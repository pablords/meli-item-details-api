package com.pablords.meli_item_details_api.adapters.inbound.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {
  @NotBlank(message = "Name cannot be empty")
  @JsonProperty
  String name;

  @NotBlank(message = "Email cannot be empty")
  @JsonProperty
  String email;
}
