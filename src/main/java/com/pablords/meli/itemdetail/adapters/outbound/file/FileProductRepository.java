package com.pablords.meli.itemdetail.adapters.outbound.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.*;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ProductRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.Money;
import com.pablords.meli.itemdetail.domain.valueobject.SearchResult;
import com.pablords.meli.itemdetail.domain.valueobject.Seller;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import java.time.Duration;

public class FileProductRepository implements ProductRepositoryPort {
  private final Map<String, Product> byId = new ConcurrentHashMap<>();
  private final Map<String, Set<String>> tokens = new ConcurrentHashMap<>();
  private final Map<String, Set<String>> byCategory = new ConcurrentHashMap<>();
  private final Map<String, Set<String>> byBrand = new ConcurrentHashMap<>();
  private final Map<String, Seller> sellers = new ConcurrentHashMap<>();
  private final Cache<String, Product> cache;

  public FileProductRepository(String productsResource, String sellersResource, Duration ttl) {
    this.cache = Caffeine.newBuilder().expireAfterWrite(ttl).maximumSize(5000).build();
    load(productsResource, sellersResource);
  }

  public Optional<Product> getById(String id) {
    var c = cache.getIfPresent(id);
    if (c != null)
      return Optional.of(c);
    var p = byId.get(id);
    if (p != null)
      cache.put(id, p);
    return Optional.ofNullable(p);
  }

  public SearchResult search(String q, int limit, int offset) {
    Set<String> ids = new LinkedHashSet<>();
    if (q == null || q.isBlank()) {
      ids.addAll(byId.keySet());
    } else {
      for (var t : tokenize(q)) {
        var set = tokens.get(t);
        if (set != null)
          ids.addAll(set);
      }
    }
    var all = ids.stream().map(byId::get).filter(Objects::nonNull).toList();
    int total = all.size();
    var page = all.stream().skip(offset).limit(limit).toList();
    return new SearchResult(page, total);
  }

  public List<Product> recommendations(String id, int limit) {
    var p = byId.get(id);
    if (p == null)
      return List.of();
    Set<String> cand = new LinkedHashSet<>();
    cand.addAll(byCategory.getOrDefault(p.getCategory().toLowerCase(), Set.of()));
    cand.addAll(byBrand.getOrDefault(p.getBrand().toLowerCase(), Set.of()));
    cand.remove(id);
    return cand.stream().limit(limit).map(byId::get).filter(Objects::nonNull).toList();
  }

  public Optional<Seller> getSellerById(String id) {
    return Optional.ofNullable(sellers.get(id));
  }

  private void load(String pRes, String sRes) {
    ObjectMapper om = new ObjectMapper();
    try (InputStream pIs = getResource(pRes); InputStream sIs = getResource(sRes)) {
      List<Map<String, Object>> raw = om.readValue(pIs, new TypeReference<>() {
      });
      for (var rp : raw) {
        var p = mapProduct(om, rp);
        byId.put(p.getId(), p);
        for (var tok : tokenize(p.getTitle()))
          tokens.computeIfAbsent(tok, k -> new LinkedHashSet<>()).add(p.getId());
        byCategory.computeIfAbsent(p.getCategory().toLowerCase(), k -> new LinkedHashSet<>()).add(p.getId());
        byBrand.computeIfAbsent(p.getBrand().toLowerCase(), k -> new LinkedHashSet<>()).add(p.getId());
      }
      List<Seller> ss = om.readValue(sIs, new TypeReference<>() {
      });
      for (var s : ss)
        sellers.put(s.id(), s);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load dataset", e);
    }
  }

  private static InputStream getResource(String path) {
    var is = FileProductRepository.class.getClassLoader().getResourceAsStream(path);
    if (is == null)
      throw new IllegalArgumentException("Resource not found: " + path);
    return is;
  }

  @SuppressWarnings("unchecked")
  private static Product mapProduct(ObjectMapper om, Map<String, Object> rp) {
    String id = (String) rp.get("id");
    String title = (String) rp.get("title");
    String brand = (String) rp.get("brand");
    String category = (String) rp.get("category");
    Map<String, Object> price = (Map<String, Object>) rp.get("price");
    Money money = new Money(((Number) price.get("amount")).doubleValue(), (String) price.get("currency"));
    String thumbnail = (String) rp.get("thumbnail");
    List<String> pictures = ((List<Object>) rp.get("pictures")).stream().map(Object::toString)
        .collect(Collectors.toList());
    Map<String, String> attrs = new HashMap<>();
    Map<String, Object> raw = (Map<String, Object>) rp.get("attributes");
    if (raw != null)
      raw.forEach((k, v) -> attrs.put(k, v.toString()));
    int available = ((Number) rp.get("available_quantity")).intValue();
    String sellerId = (String) rp.get("seller_id");
    return Product.create(id, title, brand, category, money, thumbnail, pictures, attrs, available, sellerId);
  }

  private static List<String> tokenize(String s) {
    return Arrays.stream(s.toLowerCase().split("[^a-z0-9á-ú]+")).filter(t -> !t.isBlank()).toList();
  }
}
