package com.pablords.meli.itemdetail.domain.ports.outbound.repository;

import java.util.*;

import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.valueobject.SearchResult;
import com.pablords.meli.itemdetail.domain.valueobject.Seller;

public interface ProductRepositoryPort {
  Optional<Product> getById(String id);
  SearchResult search(String query, int limit, int offset);
  List<Product> recommendations(String id, int limit);
  Optional<Seller> getSellerById(String id);
}
