package com.fifteen.eureka.common.exceptionhandler;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResErrorCode;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "ValidExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE + 1)
public class ValidExceptionHandler {

  // MethodArgumentNotValidException 처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    log.error("Validation error", exception);

    List<String> errors = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + " : " + error.getDefaultMessage())
        .collect(Collectors.toList());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.ERROR(ResErrorCode.BAD_REQUEST, "Validation failed", errors));
  }

  // ConstraintViolationException 처리
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
    log.error("Constraint violation", exception);

    List<String> errors = exception.getConstraintViolations()
        .stream()
        .map(violation -> violation.getPropertyPath() + " : " + violation.getMessage())
        .collect(Collectors.toList());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.ERROR(ResErrorCode.BAD_REQUEST, "Constraint violation", errors));
  }
}
