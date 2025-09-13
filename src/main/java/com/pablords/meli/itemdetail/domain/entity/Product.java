package com.pablords.meli.itemdetail.domain.entity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.pablords.meli.itemdetail.domain.valueobject.Money;

public final class Product {
  private final String id;
  private String title;
  private String brand;
  private String category;
  private Money price;
  private String thumbnail;
  private List<String> pictures;
  private Map<String, String> attributes;
  private int availableQuantity;
  private String sellerId;

  private Product(String id) {
    this.id = id;
  }

  public static Product create(String id, String title, String brand, String category, Money price,
      String thumbnail, List<String> pictures, Map<String, String> attributes,
      int availableQuantity, String sellerId) {

    Product p = new Product(id);
    p.title = title;
    p.brand = brand;
    p.category = category;
    p.price = price;
    p.thumbnail = thumbnail;
    p.pictures = List.copyOf(pictures);
    p.attributes = Map.copyOf(attributes);
    p.availableQuantity = availableQuantity;
    p.sellerId = sellerId;
    return p;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getBrand() {
    return brand;
  }

  public String getCategory() {
    return category;
  }

  public Money getPrice() {
    return price;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public List<String> getPictures() {
    return List.copyOf(pictures);
  }

  public Map<String, String> getAttributes() {
    return Map.copyOf(attributes); 
  }

  public int getAvailableQuantity() {
    return availableQuantity;
  }

  public String getSellerId() {
    return sellerId;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Product p) && id.equals(p.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
