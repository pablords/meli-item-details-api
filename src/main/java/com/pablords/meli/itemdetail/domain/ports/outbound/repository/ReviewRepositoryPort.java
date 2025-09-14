package com.pablords.meli.itemdetail.domain.ports.outbound.repository;

import java.util.List;

import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

public interface ReviewRepositoryPort {
  List<Review> findByProduct(String productId, ReviewSort sort, int limit, int offset);

  int totalByProduct(String productId);

  RatingSummary summaryFor(String productId);
}
