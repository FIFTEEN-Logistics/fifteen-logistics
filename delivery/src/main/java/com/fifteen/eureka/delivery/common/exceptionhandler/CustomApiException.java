package com.fifteen.eureka.delivery.common.exceptionhandler;

import com.fifteen.eureka.delivery.common.response.ResCode;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException implements
    CustomApiExceptionIfs {

  private final ResCode errorCode;
  private final String errorDescription;

  public CustomApiException(ResCode errorCode) {
    this(errorCode, errorCode.getMessage());
  }

  public CustomApiException(ResCode errorCode, String errorDescription) {
    super(errorDescription);
    this.errorCode = errorCode;
    this.errorDescription = errorDescription;
  }
}