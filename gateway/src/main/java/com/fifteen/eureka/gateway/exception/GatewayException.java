package com.fifteen.eureka.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GatewayException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final String message;

  public GatewayException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
