package com.pablords.meli.itemdetail.adapters.inbound.http.dto;

import com.pablords.meli.itemdetail.domain.entity.Product;

public record RecommendationResponseDTO(
    String id,
    String title,
    PriceResponseDTO price,
    String thumbnail) {

  public static RecommendationResponseDTO from(Product product) {
    return new RecommendationResponseDTO(
        product.getId(),
        product.getTitle(),
        PriceResponseDTO.from(product.getPrice()),
        product.getThumbnail());
  }

}
