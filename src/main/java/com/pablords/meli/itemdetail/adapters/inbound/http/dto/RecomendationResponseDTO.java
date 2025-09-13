package com.pablords.meli.itemdetail.adapters.inbound.http.dto;

import com.pablords.meli.itemdetail.domain.entity.Product;

public record RecomendationResponseDTO(
    String id,
    String title,
    PriceResponseDTO price,
    String thumbnail) {

  public static RecomendationResponseDTO from(Product product) {
    return new RecomendationResponseDTO(
        product.getId(),
        product.getTitle(),
        PriceResponseDTO.from(product.getPrice()),
        product.getThumbnail());
  }

}
