package com.pablords.meli_item_details_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {
  @JsonProperty
  String id;
  @JsonProperty
  String name;
  @JsonProperty
  String email;
}
