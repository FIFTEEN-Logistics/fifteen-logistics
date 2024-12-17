package com.fifteen.eureka.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GatewayExceptionHandler {

  @ExceptionHandler(GatewayException.class)
  public ResponseEntity<Object> handleGatewayException(GatewayException ex) {
    log.error("Gateway Exception: {}", ex.getMessage(), ex);
    return ResponseEntity
        .status(ex.getHttpStatus())
        .body(new ErrorResponse(ex.getHttpStatus().value(), ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(Exception ex) {
    log.error("Unexpected Exception: {}", ex.getMessage(), ex);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred"));
  }

  private record ErrorResponse(int statusCode, String message) {}
}
