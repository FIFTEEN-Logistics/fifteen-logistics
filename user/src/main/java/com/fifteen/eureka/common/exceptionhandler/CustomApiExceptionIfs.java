package com.fifteen.eureka.common.exceptionhandler;


import com.fifteen.eureka.common.response.ResCode;

public interface CustomApiExceptionIfs {

  ResCode getErrorCode();

  String getErrorDescription();

}
