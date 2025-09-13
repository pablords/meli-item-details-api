
package com.pablords.meli.itemdetail.adapters.inbound.http.handler;


import java.util.Map;

import org.springframework.http.HttpStatus;
import com.pablords.meli.itemdetail.domain.application.exception.NotFoundException;


public class ExceptionStatusMapper {

  private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
      IllegalArgumentException.class, HttpStatus.CONFLICT,
      NotFoundException.class, HttpStatus.NOT_FOUND);

  public static HttpStatus getStatus(Class<? extends RuntimeException> exceptionClass) {
    return EXCEPTION_STATUS_MAP.getOrDefault(exceptionClass, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
