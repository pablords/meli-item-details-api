package com.pablords.meli.itemdetail.domain.ports.outbound.repository;

import java.util.*;

import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.valueobject.Seller;

public interface ProductRepositoryPort {
  Optional<Product> getById(String id);
  List<Product> recommendations(String id, int limit);
  Optional<Seller> getSellerById(String id);
}
