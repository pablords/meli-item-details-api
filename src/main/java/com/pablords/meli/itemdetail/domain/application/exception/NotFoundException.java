package com.pablords.meli.itemdetail.domain.application.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
