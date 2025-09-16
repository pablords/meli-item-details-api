package com.pablords.meli.itemdetail.domain.application.service;

import java.util.Collections;
import java.util.List;

import com.pablords.meli.itemdetail.domain.application.exception.NotFoundException;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ProductServicePort;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ProductRepositoryPort;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ReviewRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.Paged;
import com.pablords.meli.itemdetail.domain.valueobject.ProductWithSeller;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductService implements ProductServicePort {
  private final ProductRepositoryPort productRepository;
  private final ReviewRepositoryPort reviewRepository;

  public ProductService(ProductRepositoryPort productRepository, ReviewRepositoryPort reviewRepository) {
    this.productRepository = productRepository;
    this.reviewRepository = reviewRepository;
  }

  public ProductWithSeller getProductWithSeller(String id) {
    log.info("Getting product with id: {}", id);
    var product = productRepository.getById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    log.info("Product found: {}", product.toString());
    var seller = productRepository.getSellerById(product.getSellerId()).orElse(null);
    log.info("Seller found: {}", seller != null ? seller.toString() : "No seller found");
    return new ProductWithSeller(product, seller);
  }

  public List<Product> getRecommendations(String id, int limit) {
    log.info("Getting recommendations for product id: {}", id);
    var recommendations = productRepository.recommendations(id, limit);
    log.info("Recommendations found: {}", recommendations.size());
    return recommendations;
  }

  public Paged<Review> getReviewsByProduct(String productId, ReviewSort sort, int limit, int offset) {
    log.info("Fetching reviews for product {} with sort {} and pagination {}:{}", productId, sort, limit, offset);
    var items = reviewRepository.findByProduct(productId, sort, limit, offset);
    var total = reviewRepository.totalByProduct(productId);
    log.info("Fetched {} reviews for product {} with sort {} and pagination {}:{}", items.size(), productId, sort,
        limit, offset);
    return new Paged<>(items, total, limit, offset);
  }

  public RatingSummary getRatingSummaryByProduct(String productId) {
    log.info("Fetching rating summary for product {}", productId);
    var summary = reviewRepository.summaryFor(productId);
    log.info("Fetched rating summary for product {}: {}", productId, summary);
    return summary;
  }

}
