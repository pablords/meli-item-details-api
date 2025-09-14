package com.pablords.meli.itemdetail.adapters.inbound.http.dto;

import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.valueobject.Paged;

public record ReviewResponseDTO(Paged<Review> reviews) {

}
