package com.pablords.meli.itemdetail.adapters.inbound.http.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.meli.itemdetail.adapters.inbound.http.dto.PriceResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.ProductResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.RecommendationResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.ReviewResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.SellerResponseDTO;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ProductServicePort;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController implements ProductSwagger {
  private final ProductServicePort productService;

  public ProductController(ProductServicePort productService) {
    this.productService = productService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getById(@PathVariable @NotBlank @Size(min = 1, max = 50) String id) {
    log.info("Fetching product details for id: {}", id);
    var productWithSeller = productService.getProductWithSeller(id);
    var product = productWithSeller.product();
    var seller = productWithSeller.seller();

    SellerResponseDTO sellerDto = seller != null
        ? new SellerResponseDTO(seller.id(), seller.nickname(), seller.rating())
        : null;

    PriceResponseDTO priceDto = product.getPrice() != null
        ? new PriceResponseDTO(product.getPrice().amount(), product.getPrice().currency())
        : null;

    log.info("Product details fetched successfully for id: {}", id);
    return ResponseEntity.status(HttpStatus.OK).body(new ProductResponseDTO(
        product.getId(),
        product.getTitle(),
        product.getBrand(),
        product.getCategory(),
        priceDto,
        product.getThumbnail(),
        product.getPictures().toArray(new String[0]),
        product.getAttributes(),
        product.getAvailableQuantity(),
        sellerDto));
  }

  @GetMapping("/{id}/recommendations")
  public ResponseEntity<Map<String, List<RecommendationResponseDTO>>> getRecommendations(@PathVariable String id,
      @RequestParam(defaultValue = "6") @Min(1) @Max(24) int limit) {
    log.info("Fetching product recommendations for id: {}", id);
    List<Product> items = productService.getRecommendations(id, limit);
    log.info("Product recommendations fetched successfully for id: {}", id);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("items", items.stream().map(RecommendationResponseDTO::from).toList()));
  }

  @GetMapping("/{id}/reviews")
  public ResponseEntity<ReviewResponseDTO> reviews(@PathVariable String id,
      @RequestParam(defaultValue = "recent") String sort,
      @RequestParam(defaultValue = "2") @Min(1) @Max(100) int limit,
      @RequestParam(defaultValue = "0") @Min(0) int offset) {
    log.info("Fetching reviews for product {} with sort {} and pagination {}:{}", id, sort, limit, offset);
    var s = switch (sort.toLowerCase()) {
      case "helpful" -> ReviewSort.HELPFUL;
      case "rating_desc" -> ReviewSort.RATING_DESC;
      case "rating_asc" -> ReviewSort.RATING_ASC;
      default -> ReviewSort.RECENT;
    };
    var page = productService.getReviewsByProduct(id, s, limit, offset);
    log.info("Fetched {} reviews for product {} with sort {} and pagination {}:{}", page.items().size(), id, sort, limit, offset);
    return ResponseEntity.status(HttpStatus.OK).body(new ReviewResponseDTO(page));
  }

  @GetMapping("/{id}/ratings")
  public ResponseEntity<RatingSummary> rating(@PathVariable String id) {
    log.info("Fetching rating summary for product {}", id);
    var summary = productService.getRatingSummaryByProduct(id);
    log.info("Fetched rating summary for product {}: {}", id, summary);
    return ResponseEntity.status(HttpStatus.OK).body(summary);
  }

}
