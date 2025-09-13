package com.pablords.meli.itemdetail.domain.application.service;

import java.util.Collections;
import java.util.List;

import com.pablords.meli.itemdetail.domain.application.exception.NotFoundException;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ProductServicePort;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ProductRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.ProductWithSeller;
import com.pablords.meli.itemdetail.domain.valueobject.SearchResult;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductService implements ProductServicePort {
  private final ProductRepositoryPort repository;

  public ProductService(ProductRepositoryPort repository) {
    this.repository = repository;
  }

  @CircuitBreaker(name = "productService", fallbackMethod = "getProductWithSellerFallback")
  @RateLimiter(name = "productService")
  public ProductWithSeller getProductWithSeller(String id) {
    log.info("Getting product with id: {}", id);
    var product = repository.getById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    log.info("Product found: {}", product.toString());
    var seller = repository.getSellerById(product.getSellerId()).orElse(null);
    log.info("Seller found: {}", seller != null ? seller.toString() : "No seller found");
    return new ProductWithSeller(product, seller);
  }

  public ProductWithSeller getProductWithSellerFallback(String id, Exception ex) {
    log.warn("Fallback triggered for getProductWithSeller with id: {} due to: {}", id, ex.getMessage());
    throw new NotFoundException("Product service temporarily unavailable. Please try again later.");
  }

  @CircuitBreaker(name = "productService", fallbackMethod = "getRecommendationsFallback")
  @RateLimiter(name = "productService")
  public List<Product> getRecommendations(String id, int limit) {
    log.info("Getting recommendations for product id: {}", id);
    var recommendations = repository.recommendations(id, limit);
    log.info("Recommendations found: {}", recommendations.size());
    return recommendations;
  }

  public List<Product> getRecommendationsFallback(String id, int limit, Exception ex) {
    log.warn("Fallback triggered for getRecommendations with id: {} due to: {}", id, ex.getMessage());
    return Collections.emptyList();
  }

  @CircuitBreaker(name = "productService", fallbackMethod = "getSearchFallback")
  @RateLimiter(name = "productService")
  public SearchResult getSearch(String query, int limit, int offset) {
    log.info("Searching products with query: {}, limit: {}, offset: {}", query, limit, offset);
    return repository.search(query, limit, offset);
  }

  public SearchResult getSearchFallback(String query, int limit, int offset, Exception ex) {
    log.warn("Fallback triggered for getSearch with query: {} due to: {}", query, ex.getMessage());
    return new SearchResult(Collections.emptyList(), 0);
  }

}
