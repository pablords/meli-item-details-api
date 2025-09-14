package com.pablords.meli.itemdetail.adapters.inbound.http.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.pablords.meli.itemdetail.adapters.inbound.http.dto.ReviewResponseDTO;
import com.pablords.meli.itemdetail.domain.valueobject.RatingSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "Review", description = "Operations related to reviews")
public interface ReviewSwagger {

  @Operation(summary = "Get reviews by product ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of reviews"),
      @ApiResponse(responseCode = "404", description = "Product not found")
  })
  ResponseEntity<ReviewResponseDTO> reviews(@PathVariable String id,
      @RequestParam(defaultValue = "recent") String sort,
      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
      @RequestParam(defaultValue = "0") @Min(0) int offset);

  @Operation(summary = "Get rating summary by product ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of rating summary"),
      @ApiResponse(responseCode = "404", description = "Product not found")
  })
  ResponseEntity<RatingSummary> rating(@PathVariable String id);

}
