package com.fifteen.eureka.common.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 필드 제외
@NoArgsConstructor
public class ApiResponse<T> {

  private Integer httpStatusCode;
  private Integer code;
  private String message;
  private String description;
  private List<String> errorList;
  private T data;

  private ApiResponse(ResCode resCode, String description, List<String> errorList, T data) {
    this.httpStatusCode = resCode.getHttpStatusCode();
    this.code = resCode.getCode();
    this.message = resCode.getMessage();
    this.description = description;
    this.errorList = errorList;
    this.data = data;
  }

  // 성공 응답 생성
  public static ApiResponse<Void> OK(ResCode resCode) {
    return new ApiResponse<>(resCode, null, null, null);
  }

  public static <T> ApiResponse<T> OK(ResCode resCode, T data) {
    return new ApiResponse<>(resCode, null, null, data);
  }

  public static ApiResponse<Void> OK(ResCode resCode, String description) {
    return new ApiResponse<>(resCode, description, null, null);
  }

  public static <T> ApiResponse<T> OK(ResCode resCode, T data, String description) {
    return new ApiResponse<>(resCode, description, null, data);
  }

  // 에러 응답 생성
  public static ApiResponse<Void> ERROR(ResCode resCode) {
    return new ApiResponse<>(resCode, null, null, null);
  }

  public static <T> ApiResponse<T> ERROR(ResCode resCode, String description) {
    return new ApiResponse<>(resCode, description, null, null);
  }

  public static ApiResponse<Void> ERROR(ResCode resCode, List<String> errorList) {
    return new ApiResponse<>(resCode, null, errorList, null);
  }

  public static ApiResponse<Void> ERROR(ResCode resCode, String description, List<String> errorList) {
    return new ApiResponse<>(resCode, description, errorList, null);
  }
}
