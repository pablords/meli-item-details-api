package com.pablords.meli.itemdetail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ProductServicePort;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ReviewServicePort;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ProductRepositoryPort;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ReviewRepositoryPort;
import com.pablords.meli.itemdetail.domain.application.service.ProductService;
import com.pablords.meli.itemdetail.domain.application.service.ReviewService;

@Configuration
public class AppConfig {
  @Bean
  ProductServicePort productService(ProductRepositoryPort productRepository) {
    return new ProductService(productRepository);
  }

  @Bean
  ReviewServicePort reviewService(ReviewRepositoryPort reviewRepository) {
    return new ReviewService(reviewRepository);
  }
}
