package com.pablords.meli.itemdetail.adapters.inbound.http.controller;

import org.springframework.web.bind.annotation.RequestBody;

import com.pablords.meli.itemdetail.adapters.inbound.http.dto.UserRequestDto;
import com.pablords.meli.itemdetail.adapters.inbound.http.dto.UserResponseDto;
import com.pablords.meli.itemdetail.adapters.inbound.http.handler.ApiErrorDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Product", description = "Operations related to products")
public interface ProductSwagger {

  @Operation(summary = "Get a products", tags = "Product", method = "GET", operationId = "product-get")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Valid Request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserRequestDto.class), examples = @ExampleObject(value = "{\n  \"id\": \"9f6954d3-f988-4b58-bd5f-6919d3c7713c\",\n  \"name\": \"pablo\",\n  \"email\": \"pablo@email.com\"\n}")) }),
      @ApiResponse(responseCode = "409", description = "Invalid Request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class), examples = @ExampleObject(value = "{\"error\":\"Conflict\",\"message\":\"A email already exists: jhon@email.com\",\"path\":\"/api/v1/users\",\"timestamp\":\"2025-02-01T16:57:06Z\"}")) }),
  })
  UserResponseDto create(@RequestBody @Valid UserRequestDto request);

}
