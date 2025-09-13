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
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.RecomendationResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.SellerResponseDTO;
import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ProductServicePort;
import com.pablords.meli.itemdetail.domain.entity.Product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductController implements ProductSwagger {
  private final ProductServicePort productService;

  public ProductController(ProductServicePort productService) {
    this.productService = productService;
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<ProductResponseDTO> getById(@PathVariable @NotBlank String id) {
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

  @GetMapping("/products/{id}/recommendations")
  public ResponseEntity<Map<String, List<RecomendationResponseDTO>>> recs(@PathVariable String id,
      @RequestParam(defaultValue = "6") @Min(1) @Max(24) int limit) {
    List<Product> items = productService.getRecommendations(id, limit);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("items", items.stream().map(RecomendationResponseDTO::from).toList()));
  }

}
