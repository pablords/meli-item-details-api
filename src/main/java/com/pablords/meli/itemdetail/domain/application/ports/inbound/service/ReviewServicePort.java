package com.pablords.meli.itemdetail.domain.application.ports.inbound.service;

import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.valueobject.Paged;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

public interface ReviewServicePort {
  Paged<Review> getByProduct(String productId, ReviewSort sort, int limit, int offset);
  RatingSummary getRatingSummary(String productId);
}
