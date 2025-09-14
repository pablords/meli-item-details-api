package com.pablords.meli.itemdetail.adapters.inbound.http.dto;

import com.pablords.meli.itemdetail.domain.valueobject.Money;

public record PriceResponseDTO(double amount, String currency) {
  public static PriceResponseDTO from(Money price) {
    return price != null
        ? new PriceResponseDTO(price.amount(), price.currency())
        : null;
  }
}
