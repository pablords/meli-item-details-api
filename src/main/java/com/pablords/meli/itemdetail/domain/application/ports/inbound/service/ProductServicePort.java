package com.pablords.meli.itemdetail.domain.application.ports.inbound.service;

import java.util.List;

import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.valueobject.Paged;
import com.pablords.meli.itemdetail.domain.valueobject.ProductWithSeller;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

public interface ProductServicePort {
  ProductWithSeller getProductWithSeller(String id);

  List<Product> getRecommendations(String id, int limit);

  Paged<Review> getReviewsByProduct(String productId, ReviewSort sort, int limit, int offset);

  RatingSummary getRatingSummaryByProduct(String productId);
}
