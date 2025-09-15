package com.pablords.meli.itemdetail.adapters.outbound.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ReviewRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileReviewRepositoryAdapter implements ReviewRepositoryPort {
  private final Map<String, List<Review>> recent = new HashMap<>();
  private final Map<String, List<Review>> helpful = new HashMap<>();
  private final Map<String, List<Review>> ratingDesc = new HashMap<>();
  private final Map<String, List<Review>> ratingAsc = new HashMap<>();
  private final Map<String, RatingSummary> summary = new HashMap<>();
  @Value("${dataset.products:data/reviews.json}")
  String reviewsSource;

  public FileReviewRepositoryAdapter() {
    // Construtor intencionalmente vazio: inicialização e carga ocorrem em @PostConstruct
  }

  @PostConstruct
  private void init() {
    load(reviewsSource);
  }

  @Override
  public List<Review> findByProduct(String pid, ReviewSort sort, int limit, int offset) {

    List<Review> base = switch (sort) {
      case HELPFUL -> helpful.getOrDefault(pid, List.of());
      case RATING_DESC -> ratingDesc.getOrDefault(pid, List.of());
      case RATING_ASC -> ratingAsc.getOrDefault(pid, List.of());
      case RECENT -> recent.getOrDefault(pid, List.of());
      default -> recent.getOrDefault(pid, List.of());
    };
    return base.stream().skip(offset).limit(limit).toList();
  }

  @Override
  public int totalByProduct(String pid) {
    return recent.getOrDefault(pid, List.of()).size();
  }

  @Override
  public RatingSummary summaryFor(String pid) {
    return summary.getOrDefault(pid, new RatingSummary(0, 0.0, Map.of()));
  }

  private void load(String path) {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      var om = new ObjectMapper();
      List<Map<String, Object>> rows = om.readValue(is, new TypeReference<>() {
      });
      Map<String, List<Review>> tmp = new HashMap<>();
      for (var r : rows) {
        var rev = map(r);
        tmp.computeIfAbsent(rev.getProductId(), k -> new ArrayList<>()).add(rev);
      }
      // recent/helpful
      for (var e : tmp.entrySet()) {
        var list = List.copyOf(e.getValue());
        var recentList = list.stream().sorted(Comparator.comparing(Review::getCreatedAt).reversed()).toList();
        var helpfulList = list.stream().sorted(Comparator
            .comparingInt((Review rv) -> rv.getHelpfulVotes() + (rv.isVerifiedPurchase() ? 2 : 0)).reversed()
            .thenComparing(Review::getCreatedAt, Comparator.reverseOrder())).toList();
        recent.put(e.getKey(), recentList);
        helpful.put(e.getKey(), helpfulList);
        ratingDesc.put(e.getKey(), list.stream().sorted(Comparator.comparingInt(Review::getRating).reversed()
            .thenComparing(Review::getCreatedAt, Comparator.reverseOrder())).toList());
        ratingAsc.put(e.getKey(), list.stream().sorted(Comparator.comparingInt(Review::getRating)
            .thenComparing(Review::getCreatedAt, Comparator.reverseOrder())).toList());
        summary.put(e.getKey(), computeSummary(list));
      }
    } catch (IOException ex) {
      throw new ReviewDatasetLoadException("Failed to load reviews", ex);
    }
  }

  static class ReviewDatasetLoadException extends RuntimeException {
    ReviewDatasetLoadException(String msg, Throwable cause) { super(msg, cause); }
  }


  private static Review map(Map<String, Object> r) {
  return Review.builder(
    (String) r.get("id"),
    (String) r.get("product_id"),
    (int) r.get("rating"))
    .title((String) r.get("title"))
    .body((String) r.get("body"))
    .author((String) r.get("author"))
    .verifiedPurchase((boolean) r.get("verified_purchase"))
    .helpfulVotes((int) r.get("helpful_votes"))
    .createdAt((String) r.get("created_at"))
    .locale((String) r.get("locale"))
    .build();
  }

  private static RatingSummary computeSummary(List<Review> list) {
    int count = list.size();
    if (count == 0)
      return new RatingSummary(0, 0.0, Map.of());
    double avg = list.stream().mapToInt(Review::getRating).average().orElse(0);
    Map<Integer, Integer> hist = list.stream()
        .collect(Collectors.groupingBy(Review::getRating, Collectors.summingInt(x -> 1)));
    // garantir 1..5 presentes
    for (int i = 1; i <= 5; i++)
      hist.putIfAbsent(i, 0);
    return new RatingSummary(count, Math.round(avg * 10) / 10.0, Map.copyOf(hist));
  }
}
