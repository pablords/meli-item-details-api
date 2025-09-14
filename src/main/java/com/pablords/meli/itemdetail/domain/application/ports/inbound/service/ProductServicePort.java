package com.pablords.meli.itemdetail.domain.application.ports.inbound.service;

import java.util.List;

import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.valueobject.ProductWithSeller;

public interface ProductServicePort {
  ProductWithSeller getProductWithSeller(String id);
  List<Product> getRecommendations(String id, int limit);
}
