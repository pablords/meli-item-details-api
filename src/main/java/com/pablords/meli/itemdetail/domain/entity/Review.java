package com.pablords.meli.itemdetail.domain.entity;

import java.util.Objects;

public class Review {
  private final String id;
  private String productId;
  private int rating;
  private String title;
  private String body;
  private String author;
  private boolean verifiedPurchase;
  private int helpfulVotes;
  private String createdAt;
  private String locale;

  private Review(String id) {
    this.id = id;
  }

  public static Review create(String id, String productId, int rating, String title, String body, String author,
      boolean verifiedPurchase, int helpfulVotes, String createdAt, String locale) {
    Review review = new Review(id);
    review.productId = productId;
    review.rating = rating;
    review.title = title;
    review.body = body;
    review.author = author;
    review.verifiedPurchase = verifiedPurchase;
    review.helpfulVotes = helpfulVotes;
    review.createdAt = createdAt;
    review.locale = locale;
    return review;
  }

  public String getId() {
    return id;
  }

  public String getProductId() {
    return productId;
  }

  public int getRating() {
    return rating;
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }

  public String getAuthor() {
    return author;
  }

  public boolean isVerifiedPurchase() {
    return verifiedPurchase;
  }

  public int getHelpfulVotes() {
    return helpfulVotes;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getLocale() {
    return locale;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Review r) && id.equals(r.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}