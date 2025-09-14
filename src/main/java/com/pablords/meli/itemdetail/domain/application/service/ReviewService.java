package com.pablords.meli.itemdetail.domain.application.service;


import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ReviewServicePort;
import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ReviewRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.Paged;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReviewService implements ReviewServicePort {
  private final ReviewRepositoryPort repository;

  public ReviewService(ReviewRepositoryPort repository) {
    this.repository = repository;
  }

  public Paged<Review> getByProduct(String productId, ReviewSort sort, int limit, int offset) {
    log.info("Fetching reviews for product {} with sort {} and pagination {}:{}", productId, sort, limit, offset);
    var items = repository.findByProduct(productId, sort, limit, offset);
    var total = repository.totalByProduct(productId);
    log.info("Fetched {} reviews for product {} with sort {} and pagination {}:{}", items.size(), productId, sort, limit, offset);
    return new Paged<>(items, total, limit, offset);
  }

  public RatingSummary getRatingSummary(String productId) {
    log.info("Fetching rating summary for product {}", productId);
    var summary = repository.summaryFor(productId);
    log.info("Fetched rating summary for product {}: {}", productId, summary);
    return summary;
  }
 
}
