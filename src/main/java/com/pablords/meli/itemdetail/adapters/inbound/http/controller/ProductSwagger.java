package com.pablords.meli.itemdetail.adapters.inbound.http.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.pablords.meli.itemdetail.adapters.inbound.http.dto.ProductResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.RecommendationResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.ReviewResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.handler.ApiErrorDTO;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Tag(name = "Products", description = "Operations related to products")
public interface ProductSwagger {

  @Operation(summary = "Get product by ID", method = "GET", operationId = "get-product-by-id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Product found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))
      }),
      @ApiResponse(responseCode = "404", description = "Product not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class)) }),
  })
  ResponseEntity<ProductResponseDTO> getById(@PathVariable @NotBlank @Size(min = 1, max = 50) String id);

  @Operation(summary = "Get recommendations by product ID", method = "GET", operationId = "get-recommendations-by-product-id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Recommendations found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendationResponseDTO.class))
      })
  })
  ResponseEntity<Map<String, List<RecommendationResponseDTO>>> getRecommendations(@PathVariable String id,
      @RequestParam(defaultValue = "6") @Min(1) @Max(24) int limit);

  @Operation(summary = "Get reviews by product ID", method = "GET", operationId = "get-reviews-by-product-id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of reviews"),
      @ApiResponse(responseCode = "404", description = "Product not found")
  })
  ResponseEntity<ReviewResponseDTO> reviews(@PathVariable String id,
      @RequestParam(defaultValue = "recent") String sort,
      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
      @RequestParam(defaultValue = "0") @Min(0) int offset);

  @Operation(summary = "Get rating summary by product ID", method = "GET", operationId = "get-rating-summary-by-product-id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of rating summary"),
      @ApiResponse(responseCode = "404", description = "Product not found")
  })
  ResponseEntity<RatingSummary> rating(@PathVariable String id);

}
