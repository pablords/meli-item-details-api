package com.pablords.meli_item_details_api.adapters.inbound.http.controller;

import org.springframework.web.bind.annotation.RequestBody;

import com.pablords.meli_item_details_api.adapters.inbound.http.dto.UserRequestDto;
import com.pablords.meli_item_details_api.adapters.inbound.http.dto.UserResponseDto;
import com.pablords.meli_item_details_api.adapters.inbound.http.handler.ApiError;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User", description = "Operations related to users")
public interface UserSwagger {

  @Operation(summary = "Create a user", tags = "User", method = "POST", operationId = "create")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Valid Request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserRequestDto.class), examples = @ExampleObject(value = "{\n  \"id\": \"9f6954d3-f988-4b58-bd5f-6919d3c7713c\",\n  \"name\": \"pablo\",\n  \"email\": \"pablo@email.com\"\n}")) }),
      @ApiResponse(responseCode = "409", description = "Invalid Request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(value = "{\"error\":\"Conflict\",\"message\":\"A email already exists: jhon@email.com\",\"path\":\"/api/v1/users\",\"timestamp\":\"2025-02-01T16:57:06Z\"}")) }),
      @ApiResponse(responseCode = "422", description = "Erros de validação", content = {
          @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"error\": \"Unprocessable Entity\",\n  \"errors\": {\n    \"name\": \"Name cannot be empty\"\n  },\n  \"message\": \"Validation error\",\n  \"path\": \"/api/v1/users\",\n  \"timestamp\": \"2015-01-01T13:00:00Z\"\n}")) }),
  })
  UserResponseDto create(@RequestBody @Valid UserRequestDto request);

}
