package com.fifteen.eureka.common.role;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAspect {

  @Around("@annotation(roleCheck)")
  public Object checkRole(ProceedingJoinPoint joinPoint, RoleCheck roleCheck) throws Throwable {
    HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
        RequestContextHolder.getRequestAttributes())).getRequest();
    String role = request.getHeader("X-Role");

    // 요청 헤더에서 role 값이 없거나 유효하지 않을 경우 예외 처리
    if (role == null || role.isEmpty()) {
      throw new CustomApiException(
          ResErrorCode.UNAUTHORIZED, "Missing or invalid role in header"
      );
    }

    try {
      // 요청의 역할을 Enum으로 변환
      Role userRole = Role.valueOf(role.toUpperCase());

      // 허용된 역할 목록 확인
      for (String allowedRole : roleCheck.value()) {
        if (Role.valueOf(allowedRole.toUpperCase()) == userRole) {
          return joinPoint.proceed(); // 허용된 역할일 경우 메서드 실행
        }
      }
    } catch (IllegalArgumentException e) {
      throw new CustomApiException(
          ResErrorCode.FORBIDDEN, "Invalid role format"
      );
    }

    // 허용된 역할이 아닐 경우 예외 처리
    throw new CustomApiException(ResErrorCode.FORBIDDEN, "Access denied for role: " + role);
  }
}