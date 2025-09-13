package com.pablords.meli.itemdetail.domain.valueobject;

import java.util.List;

import com.pablords.meli.itemdetail.domain.entity.Product;

public record SearchResult(List<Product> items, int total) {
}
