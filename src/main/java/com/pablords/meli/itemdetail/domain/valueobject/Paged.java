package com.pablords.meli.itemdetail.domain.valueobject;

import java.util.List;

public record Paged<T>(List<T> items, int total, int limit, int offset) {
}
