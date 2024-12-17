package com.fifteen.eureka.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResSuccessCode implements ResCode {
  SUCCESS(HttpStatus.OK.value(),  20000,"Success"),
  FETCHED(HttpStatus.OK.value(), 20001, "Fetched Successfully"),
  UPDATED(HttpStatus.OK.value(), 20002, "Updated Successfully"),
  DELETED(HttpStatus.OK.value(), 20003, "Deleted Successfully"),
  CREATED(HttpStatus.OK.value(), 20004, "Created successfully");

  private final Integer httpStatusCode;
  private final Integer code;
  private final String message;
}
