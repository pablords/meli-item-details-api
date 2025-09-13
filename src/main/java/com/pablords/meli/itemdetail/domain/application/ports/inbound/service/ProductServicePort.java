package com.pablords.meli.itemdetail.domain.application.ports.inbound.service;

import java.util.List;

import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.valueobject.ProductWithSeller;
import com.pablords.meli.itemdetail.domain.valueobject.SearchResult;

public interface ProductServicePort {
  ProductWithSeller getProductWithSeller(String id);
  List<Product> getRecommendations(String id, int limit);
  SearchResult getSearch(String query, int limit, int offset);
}
