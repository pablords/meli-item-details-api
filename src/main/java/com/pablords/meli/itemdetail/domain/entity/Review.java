package com.pablords.meli.itemdetail.domain.entity;

import java.util.Objects;

public final class Review {
  private final String id;
  private final String productId;
  private final int rating;
  private final String title;
  private final String body;
  private final String author;
  private final boolean verifiedPurchase;
  private final int helpfulVotes;
  private final String createdAt;
  private final String locale;

  @SuppressWarnings("java:S107") // Aggregate has many intrinsic fields.
  private Review(String id, String productId, int rating, String title, String body, String author,
                 boolean verifiedPurchase, int helpfulVotes, String createdAt, String locale) {
    this.id = id;
    this.productId = productId;
    this.rating = rating;
    this.title = title;
    this.body = body;
    this.author = author;
    this.verifiedPurchase = verifiedPurchase;
    this.helpfulVotes = helpfulVotes;
    this.createdAt = createdAt;
    this.locale = locale;
  }


  public static Builder builder(String id, String productId, int rating) {
    return new Builder(id, productId, rating);
  }

  public static final class Builder {
    private final String id;
    private final String productId;
    private final int rating;
    private String title;
    private String body;
    private String author;
    private boolean verifiedPurchase;
    private int helpfulVotes;
    private String createdAt;
    private String locale;

    private Builder(String id, String productId, int rating) {
      this.id = Objects.requireNonNull(id, "id is required").trim();
      this.productId = Objects.requireNonNull(productId, "productId is required").trim();
      if (rating < 1 || rating > 5) {
        throw new IllegalArgumentException("rating must be between 1 and 5");
      }
      this.rating = rating;
    }

    public Builder title(String title) { this.title = title; return this; }
    public Builder body(String body) { this.body = body; return this; }
    public Builder author(String author) { this.author = author; return this; }
    public Builder verifiedPurchase(boolean vp) { this.verifiedPurchase = vp; return this; }
    public Builder helpfulVotes(int hv) { this.helpfulVotes = Math.max(0, hv); return this; }
    public Builder createdAt(String createdAt) { this.createdAt = createdAt; return this; }
    public Builder locale(String locale) { this.locale = locale; return this; }

    public Review build() {
      return new Review(id, productId, rating, title, body, author, verifiedPurchase, helpfulVotes, createdAt, locale);
    }
  }

  public String getId() {
    return id;
  }

  public String getProductId() { return productId; }

  public int getRating() { return rating; }

  public String getTitle() { return title; }

  public String getBody() { return body; }

  public String getAuthor() { return author; }

  public boolean isVerifiedPurchase() { return verifiedPurchase; }

  public int getHelpfulVotes() { return helpfulVotes; }

  public String getCreatedAt() { return createdAt; }

  public String getLocale() { return locale; }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Review r) && id.equals(r.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}