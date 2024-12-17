package com.fifteen.eureka.common.auditor;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditorProvider {

  @Getter
  private static AuditorAwareImpl auditorAware;

  @Autowired
  public AuditorProvider(AuditorAwareImpl auditorAware) {
    AuditorProvider.auditorAware = auditorAware;
  }
}
