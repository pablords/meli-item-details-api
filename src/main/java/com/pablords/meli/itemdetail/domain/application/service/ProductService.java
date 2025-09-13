package com.pablords.meli.itemdetail.domain.application.service;

import java.util.List;

import com.pablords.meli.itemdetail.domain.application.exception.NotFoundException;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ProductServicePort;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ProductRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.ProductWithSeller;
import com.pablords.meli.itemdetail.domain.valueobject.SearchResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductService implements ProductServicePort {
  private final ProductRepositoryPort repository;

  public ProductService(ProductRepositoryPort repository) {
    this.repository = repository;
  }

  public ProductWithSeller getProductWithSeller(String id) {
    log.info("Getting product with id: {}", id);
    var product = repository.getById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    log.info("Product found: {}", product.toString());
    var seller = repository.getSellerById(product.getSellerId()).orElse(null);
    log.info("Seller found: {}", seller != null ? seller.toString() : "No seller found");
    return new ProductWithSeller(product, seller);
  }

  public List<Product> getRecommendations(String id, int limit) {
    log.info("Getting recommendations for product id: {}", id);
    return repository.recommendations(id, limit);
  }

  public SearchResult getSearch(String query, int limit, int offset) {
    log.info("Searching products with query: {}, limit: {}, offset: {}", query, limit, offset);
    return repository.search(query, limit, offset);
  }

}
