package com.fifteen.eureka.common.response;

public interface ResCode {

  Integer getHttpStatusCode();
  Integer getCode();
  String getMessage();
}
