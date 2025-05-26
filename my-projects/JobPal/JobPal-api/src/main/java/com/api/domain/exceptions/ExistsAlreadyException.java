package com.api.domain.exceptions;

public class ExistsAlreadyException extends Exception {
  private static final long serialVersionUID = 1L;

  public ExistsAlreadyException(String message) {
    super(message);
  }

  public ExistsAlreadyException(String message, Throwable cause) {
    super(message, cause);
  }
}
