package com.pablords.meli.itemdetail.domain.entity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.pablords.meli.itemdetail.domain.valueobject.Money;


public final class Product {
  private final String id;
  private final String title;
  private final String brand;
  private final String category;
  private final Money price;
  private final String thumbnail;
  private final List<String> pictures;
  private final Map<String, String> attributes;
  private final int availableQuantity;
  private final String sellerId;

  @SuppressWarnings("java:S107")
  private Product(String id, String title, String brand, String category, Money price,
      String thumbnail, List<String> pictures, Map<String, String> attributes,
      int availableQuantity, String sellerId) {
    this.id = id;
    this.title = title;
    this.brand = brand;
    this.category = category;
    this.price = price;
    this.thumbnail = thumbnail;
    this.pictures = pictures;
    this.attributes = attributes;
    this.availableQuantity = availableQuantity;
    this.sellerId = sellerId;
  }


  public static Builder builder(String id, String title, String category) {
    return new Builder(id, title, category);
  }

  public static final class Builder {
    private final String id;
    private final String title;
    private final String category;
    private String brand;
    private Money price;
    private String thumbnail;
    private List<String> pictures = List.of();
    private Map<String, String> attributes = Map.of();
    private int availableQuantity = 0;
    private String sellerId;

    private Builder(String id, String title, String category) {
      this.id = Objects.requireNonNull(id, "id is required").trim();
      this.title = Objects.requireNonNull(title, "title is required").trim();
      this.category = Objects.requireNonNull(category, "category is required").trim();
    }

    public Builder brand(String brand) {
      this.brand = brand;
      return this;
    }

    public Builder price(Money price) {
      this.price = price;
      return this;
    }

    public Builder thumbnail(String thumbnail) {
      this.thumbnail = thumbnail;
      return this;
    }

    public Builder pictures(List<String> pictures) {
      this.pictures = pictures == null ? List.of() : List.copyOf(pictures);
      return this;
    }

    public Builder attributes(Map<String, String> attributes) {
      this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
      return this;
    }

    public Builder availableQuantity(int qty) {
      if (qty < 0) {
        throw new IllegalArgumentException("availableQuantity must be >= 0");
      }
      this.availableQuantity = qty;
      return this;
    }

    public Builder sellerId(String sellerId) {
      this.sellerId = sellerId;
      return this;
    }

    public Product build() {
      return new Product(id, title, brand, category, price, thumbnail, pictures, attributes, availableQuantity,
          sellerId);
    }
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
    return pictures;
  }

  public Map<String, String> getAttributes() {
    return attributes;
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

  @Override
  public String toString() {
    return "Product{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", brand='" + brand + '\'' +
        ", category='" + category + '\'' +
        ", price=" + price +
        ", availableQuantity=" + availableQuantity +
        ", sellerId='" + sellerId + '\'' +
        '}';
  }
}
