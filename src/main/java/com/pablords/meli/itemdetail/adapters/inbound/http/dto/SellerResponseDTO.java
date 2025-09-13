package com.pablords.meli.itemdetail.adapters.inbound.http.dto;

import com.pablords.meli.itemdetail.domain.valueobject.Seller;

public record SellerResponseDTO(
    String id,
    String nickname,
    double rating) {
  public static SellerResponseDTO from(Seller seller) {
    return seller != null
        ? new SellerResponseDTO(seller.id(), seller.nickname(), seller.rating())
        : null;
  }
}
