package com.fifteen.eureka.delivery.common.auditor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(nullable = false, updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(nullable = false)
  private String updatedBy;

  private LocalDateTime deletedAt;

  private String deletedBy;

  @Column(nullable = false)
  private boolean isDeleted = false;

  @PreUpdate
  protected void onPreUpdate() {
    if (isDeleted && deletedAt == null) {
      deletedAt = LocalDateTime.now();
      // AuditorAware 빈을 통해 현재 사용자 정보 가져오기
      deletedBy = AuditorProvider.getAuditorAware().getCurrentAuditor().orElse("system");
    }
  }
}

