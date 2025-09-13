
package com.pablords.meli.itemdetail.adapters.inbound.http.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductResponseDTO(
    String id,
    String title,
    String brand,
    String category,
    PriceResponseDTO price,
    String thumbnail,
    String[] pictures,
    Map<String, String> attributes,
    @JsonProperty("available_quantity") int availableQuantity,
    SellerResponseDTO seller
    ) {


}
