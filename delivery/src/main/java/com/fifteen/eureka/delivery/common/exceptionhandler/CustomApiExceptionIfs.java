package com.fifteen.eureka.delivery.common.exceptionhandler;

import com.fifteen.eureka.delivery.common.response.ResCode;

public interface CustomApiExceptionIfs {

  ResCode getErrorCode();

  String getErrorDescription();

}
