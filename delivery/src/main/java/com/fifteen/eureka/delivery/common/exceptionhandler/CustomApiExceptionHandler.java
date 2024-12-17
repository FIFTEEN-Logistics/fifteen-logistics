package com.fifteen.eureka.delivery.common.exceptionhandler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fifteen.eureka.delivery.common.response.ApiResponse;
import com.fifteen.eureka.delivery.common.response.ResCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CustomApiExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class CustomApiExceptionHandler {

  @ExceptionHandler(value = CustomApiException.class)
  public ResponseEntity<ApiResponse<Object>> apiException(CustomApiException customApiException) {
    log.error("Custom API Exception", customApiException);

    ResCode errorCode = customApiException.getErrorCode();

    return ResponseEntity
        .status(errorCode.getHttpStatusCode())
        .body(ApiResponse.ERROR(errorCode, customApiException.getErrorDescription()));
  }
}