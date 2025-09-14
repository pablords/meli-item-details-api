package com.pablords.meli.itemdetail.domain.valueobject;

import java.util.Map;

public record RatingSummary(int count, double average, Map<Integer, Integer> histogram) {
}
