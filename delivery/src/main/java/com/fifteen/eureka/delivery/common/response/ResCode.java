package com.fifteen.eureka.delivery.common.response;

public interface ResCode {

  Integer getHttpStatusCode();
  Integer getCode();
  String getMessage();
}
