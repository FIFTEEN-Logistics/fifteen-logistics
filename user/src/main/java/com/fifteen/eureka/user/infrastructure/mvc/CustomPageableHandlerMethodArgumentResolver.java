package com.fifteen.eureka.user.infrastructure.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    // Pageable 타입에 대해 적용
    return Pageable.class.equals(parameter.getParameterType());
  }

  @Override
  public Pageable resolveArgument(MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {

    // 기본 페이지와 크기를 가져오거나 기본값으로 설정
    String page = webRequest.getParameter("page");
    String size = webRequest.getParameter("size");

    int pageNumber = (page != null) ? Integer.parseInt(page) : 0;
    int pageSize = (size != null) ? Integer.parseInt(size) : 10;

    // pageSize 제한 설정
    pageSize = (pageSize == 10 || pageSize == 30 || pageSize == 50) ? pageSize : 10;

    return PageRequest.of(pageNumber, pageSize);
  }
}
