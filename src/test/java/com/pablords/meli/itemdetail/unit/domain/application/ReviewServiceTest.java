package com.pablords.meli.itemdetail.unit.domain.application;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pablords.meli.itemdetail.domain.application.ports.inbound.service.ReviewServicePort;
import com.pablords.meli.itemdetail.domain.application.service.ReviewService;
import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ReviewRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService Unit Tests")
class ReviewServiceTest {
  private ReviewServicePort service;
  private List<Review> items;

  @Mock
  private ReviewRepositoryPort repository;

  @BeforeEach
  void setUp() {
    var review1 = Review.builder("1", "1", 3)
        .title("first")
        .author("author1")
        .verifiedPurchase(false)
        .helpfulVotes(0)
        .createdAt(LocalDateTime.now().toString())
        .locale("pt-BR")
        .build();

    var review2 = Review.builder("2", "1", 4)
        .title("second")
        .author("author2")
        .verifiedPurchase(false)
        .helpfulVotes(0)
        .createdAt(LocalDateTime.now().toString())
        .locale("pt-BR")
        .build();

    items = List.of(review1, review2);
    service = new ReviewService(repository);

    when(repository.findByProduct("1", ReviewSort.RECENT, 2, 1)).thenReturn(items);
    when(repository.totalByProduct("1")).thenReturn(2);
  }

  @Nested
  @DisplayName("findByProduct")
  class FindByProduct {
    @DisplayName("should return paged reviews when successful")
    @Test
    void shouldReturnPagedReviewsWhenSuccessful() {
      var result = service.getByProduct("1", ReviewSort.RECENT, 2, 1);
      assert result != null;
      assert result.items() != null;
      assert result.items().size() == 2;
      assert result.total() == 2;
      assert result.limit() == 2;
      assert result.offset() == 1;
      assert result.items().get(0).getId().equals("1");
      assert result.items().get(1).getId().equals("2");
    }
  }

}
