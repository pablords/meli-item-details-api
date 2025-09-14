package com.pablords.meli.itemdetail.unit.domain.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pablords.meli.itemdetail.domain.application.exception.NotFoundException;
import com.pablords.meli.itemdetail.domain.application.service.ProductService;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ProductRepositoryPort;
import com.pablords.meli.itemdetail.domain.valueobject.Money;
import com.pablords.meli.itemdetail.domain.valueobject.ProductWithSeller;
import com.pablords.meli.itemdetail.domain.valueobject.Seller;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

  @Mock
  private ProductRepositoryPort productRepository;

  private ProductService productService;

  @BeforeEach
  void setUp() {
    productService = new ProductService(productRepository);
  }

  // Test data factory methods
  private Product createTestProduct(String id, String sellerId) {
    return Product.create(
        id,
        "Test Product",
        "Test Brand",
        "Test Category",
        new Money(999.99, "BRL"),
        "http://example.com/thumbnail.jpg",
        List.of("http://example.com/pic1.jpg", "http://example.com/pic2.jpg"),
        Map.of("Color", "Blue", "Size", "L"),
        10,
        sellerId);
  }

  private Seller createTestSeller(String id) {
    return new Seller(id, "TestSeller_" + id, 4.8);
  }

  @Nested
  @DisplayName("getProductWithSeller() - Happy Path")
  class GetProductWithSellerHappyPath {

    @Test
    @DisplayName("should return ProductWithSeller when product and seller exist")
    void shouldReturnProductWithSellerWhenBothExist() {
      // Arrange
      String productId = "PRODUCT_001";
      String sellerId = "SELLER_001";
      Product expectedProduct = createTestProduct(productId, sellerId);
      Seller expectedSeller = createTestSeller(sellerId);

      when(productRepository.getById(productId)).thenReturn(Optional.of(expectedProduct));
      when(productRepository.getSellerById(sellerId)).thenReturn(Optional.of(expectedSeller));

      // Act
      ProductWithSeller result = productService.getProductWithSeller(productId);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.product()).isEqualTo(expectedProduct);
      assertThat(result.seller()).isEqualTo(expectedSeller);

      verify(productRepository).getById(productId);
      verify(productRepository).getSellerById(sellerId);
      verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("should return ProductWithSeller with null seller when seller does not exist")
    void shouldReturnProductWithNullSellerWhenSellerDoesNotExist() {
      // Arrange
      String productId = "PRODUCT_001";
      String sellerId = "SELLER_001";
      Product expectedProduct = createTestProduct(productId, sellerId);

      when(productRepository.getById(productId)).thenReturn(Optional.of(expectedProduct));
      when(productRepository.getSellerById(sellerId)).thenReturn(Optional.empty());

      // Act
      ProductWithSeller result = productService.getProductWithSeller(productId);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.product()).isEqualTo(expectedProduct);
      assertThat(result.seller()).isNull();

      verify(productRepository).getById(productId);
      verify(productRepository).getSellerById(sellerId);
      verifyNoMoreInteractions(productRepository);
    }
  }

  @Nested
  @DisplayName("getProductWithSeller() - Error Path")
  class GetProductWithSellerErrorPath {

    @Test
    @DisplayName("should throw NotFoundException when product does not exist")
    void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
      // Arrange
      String nonExistentProductId = "NON_EXISTENT";
      when(productRepository.getById(nonExistentProductId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> productService.getProductWithSeller(nonExistentProductId))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Product not found");

      verify(productRepository).getById(nonExistentProductId);
      verify(productRepository, never()).getSellerById(any());
      verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("should throw NotFoundException with null productId")
    void shouldThrowNotFoundExceptionWithNullProductId() {
      // Arrange
      when(productRepository.getById(null)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> productService.getProductWithSeller(null))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Product not found");

      verify(productRepository).getById(null);
      verify(productRepository, never()).getSellerById(any());
      verifyNoMoreInteractions(productRepository);
    }
  }

  @Nested
  @DisplayName("getRecommendations() - Happy Path")
  class GetRecommendationsHappyPath {

    @Test
    @DisplayName("should return recommendations when they exist")
    void shouldReturnRecommendationsWhenTheyExist() {
      // Arrange
      String productId = "PRODUCT_001";
      int limit = 5;
      List<Product> expectedRecommendations = List.of(
          createTestProduct("REC_001", "SELLER_002"),
          createTestProduct("REC_002", "SELLER_003"),
          createTestProduct("REC_003", "SELLER_004"));

      when(productRepository.recommendations(productId, limit))
          .thenReturn(expectedRecommendations);

      // Act
      List<Product> result = productService.getRecommendations(productId, limit);

      // Assert
      assertThat(result)
          .isNotNull()
          .hasSize(3)
          .containsExactlyElementsOf(expectedRecommendations);

      verify(productRepository).recommendations(productId, limit);
      verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("should return empty list when no recommendations exist")
    void shouldReturnEmptyListWhenNoRecommendationsExist() {
      // Arrange
      String productId = "PRODUCT_ISOLATED";
      int limit = 5;

      when(productRepository.recommendations(productId, limit))
          .thenReturn(List.of());

      // Act
      List<Product> result = productService.getRecommendations(productId, limit);

      // Assert
      assertThat(result)
          .isNotNull()
          .isEmpty();

      verify(productRepository).recommendations(productId, limit);
      verifyNoMoreInteractions(productRepository);
    }
  }

  @Nested
  @DisplayName("getRecommendations() - Boundaries")
  class GetRecommendationsBoundaries {

    @ParameterizedTest
    @MethodSource("limitBoundaryValues")
    @DisplayName("should handle different limit boundary values correctly")
    void shouldHandleLimitBoundaryValuesCorrectly(int limit, int expectedCallLimit) {
      // Arrange
      String productId = "PRODUCT_001";
      List<Product> mockRecommendations = List.of(createTestProduct("REC_001", "SELLER_001"));

      when(productRepository.recommendations(productId, expectedCallLimit))
          .thenReturn(mockRecommendations);

      // Act
      List<Product> result = productService.getRecommendations(productId, limit);

      // Assert
      assertThat(result).isNotNull();
      verify(productRepository).recommendations(productId, expectedCallLimit);
      verifyNoMoreInteractions(productRepository);
    }

    static Stream<Arguments> limitBoundaryValues() {
      return Stream.of(
          Arguments.of(0, 0), // Zero limit
          Arguments.of(1, 1), // Minimum positive limit
          Arguments.of(10, 10), // Normal limit
          Arguments.of(100, 100), // Large limit
          Arguments.of(-1, -1) // Negative limit (delegates to repository)
      );
    }
  }

  @Nested
  @DisplayName("Integration scenarios")
  class IntegrationScenarios {

    @Test
    @DisplayName("should handle complete product lifecycle operations")
    void shouldHandleCompleteProductLifecycleOperations() {
      // Arrange
      String productId = "PRODUCT_LIFECYCLE";
      String sellerId = "SELLER_LIFECYCLE";
      Product product = createTestProduct(productId, sellerId);
      Seller seller = createTestSeller(sellerId);

      // Setup mocks for different operations
      when(productRepository.getById(productId)).thenReturn(Optional.of(product));
      when(productRepository.getSellerById(sellerId)).thenReturn(Optional.of(seller));
      when(productRepository.recommendations(productId, 5))
          .thenReturn(List.of(createTestProduct("REC_001", "SELLER_002")));


      // Act - Perform multiple operations
      ProductWithSeller productDetail = productService.getProductWithSeller(productId);
      List<Product> recommendations = productService.getRecommendations(productId, 5);
  

      // Assert
      assertThat(productDetail.product()).isEqualTo(product);
      assertThat(productDetail.seller()).isEqualTo(seller);
      assertThat(recommendations).hasSize(1);

      // Verify all interactions
      verify(productRepository).getById(productId);
      verify(productRepository).getSellerById(sellerId);
      verify(productRepository).recommendations(productId, 5);

      verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("should maintain consistent behavior across multiple calls")
    void shouldMaintainConsistentBehaviorAcrossMultipleCalls() {
      // Arrange
      String productId = "CONSISTENT_PRODUCT";
      when(productRepository.recommendations(productId, 3))
          .thenReturn(List.of(createTestProduct("REC_001", "SELLER_001")));

      // Act - Multiple calls to the same operation
      List<Product> firstCall = productService.getRecommendations(productId, 3);
      List<Product> secondCall = productService.getRecommendations(productId, 3);
      List<Product> thirdCall = productService.getRecommendations(productId, 3);

      // Assert - All calls should return the same result
      assertThat(firstCall).isEqualTo(secondCall).isEqualTo(thirdCall);
      assertThat(firstCall).hasSize(1);

      // Verify repository was called exactly 3 times
      verify(productRepository, times(3)).recommendations(productId, 3);
      verifyNoMoreInteractions(productRepository);
    }
  }
}