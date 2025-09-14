package com.pablords.meli.itemdetail.adapters.inbound.http.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.meli.itemdetail.adapters.inbound.http.dto.ReviewResponseDTO;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ReviewServicePort;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController implements ReviewSwagger {
  private final ReviewServicePort reviewService;

  public ReviewController(ReviewServicePort reviewService) {
    this.reviewService = reviewService;
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<ReviewResponseDTO> reviews(@PathVariable String id,
      @RequestParam(defaultValue = "recent") String sort,
      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
      @RequestParam(defaultValue = "0") @Min(0) int offset) {
    log.info("Fetching reviews for product {} with sort {} and pagination {}:{}", id, sort, limit, offset);
    var s = switch (sort.toLowerCase()) {
      case "helpful" -> ReviewSort.HELPFUL;
      case "rating_desc" -> ReviewSort.RATING_DESC;
      case "rating_asc" -> ReviewSort.RATING_ASC;
      default -> ReviewSort.RECENT;
    };
    var page = reviewService.getByProduct(id, s, limit, offset);
    log.info("Fetched {} reviews for product {} with sort {} and pagination {}:{}", page.items().size(), id, sort, limit, offset);
    return ResponseEntity.status(HttpStatus.OK).body(new ReviewResponseDTO(page));
  }

  @GetMapping("/ratings/products/{id}")
  public ResponseEntity<RatingSummary> rating(@PathVariable String id) {
    log.info("Fetching rating summary for product {}", id);
    var summary = reviewService.getRatingSummary(id);
    log.info("Fetched rating summary for product {}: {}", id, summary);
    return ResponseEntity.status(HttpStatus.OK).body(summary);
  }
}
