package com.pablords.meli_item_details_api.adapters.http.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;


public class ExceptionStatusMapper {

  private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
      IllegalArgumentException.class, HttpStatus.CONFLICT);

  public static HttpStatus getStatus(Class<? extends RuntimeException> exceptionClass) {
    return EXCEPTION_STATUS_MAP.getOrDefault(exceptionClass, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
