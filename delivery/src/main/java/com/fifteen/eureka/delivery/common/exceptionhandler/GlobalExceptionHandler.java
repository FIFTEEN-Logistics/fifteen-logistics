package com.fifteen.eureka.delivery.common.exceptionhandler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fifteen.eureka.delivery.common.response.ApiResponse;
import com.fifteen.eureka.delivery.common.response.ResErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GlobalExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception exception) {

    log.error("Unhandled exception occurred: {}", exception.getMessage(), exception);

    return ResponseEntity
        .status(500)
        .body(ApiResponse.ERROR(ResErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
  }
}