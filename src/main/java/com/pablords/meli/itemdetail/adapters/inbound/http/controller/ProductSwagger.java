package com.pablords.meli.itemdetail.adapters.inbound.http.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.pablords.meli.itemdetail.adapters.inbound.http.dto.ProductResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.RecommendationResponseDTO;
import com.pablords.meli.itemdetail.adapters.inbound.http.handler.ApiErrorDTO;

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

@Tag(name = "Product", description = "Operations related to products")
public interface ProductSwagger {

  @Operation(summary = "Get a products", tags = "Product", method = "GET", operationId = "product-get")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Product found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))
      }),
      @ApiResponse(responseCode = "404", description = "Product not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class)) }),
  })
  ResponseEntity<ProductResponseDTO> getById(@PathVariable @NotBlank @Size(min = 1, max = 50) String id);

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Recommendations found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendationResponseDTO.class))
      })
  })
  ResponseEntity<Map<String, List<RecommendationResponseDTO>>> getRecommendations(@PathVariable String id,
      @RequestParam(defaultValue = "6") @Min(1) @Max(24) int limit);

}
