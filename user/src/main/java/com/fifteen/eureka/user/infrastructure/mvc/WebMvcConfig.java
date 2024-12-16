package com.fifteen.eureka.user.infrastructure.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final CustomPageableHandlerMethodArgumentResolver pageableResolver;

  public WebMvcConfig(CustomPageableHandlerMethodArgumentResolver pageableResolver) {
    this.pageableResolver = pageableResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(pageableResolver);
  }
}
