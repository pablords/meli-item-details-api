package com.pablords.meli.itemdetail.domain.valueobject;

import com.pablords.meli.itemdetail.domain.entity.Product;

public record ProductWithSeller(Product product, Seller seller) {
}
