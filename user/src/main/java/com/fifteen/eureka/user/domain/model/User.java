package com.fifteen.eureka.user.domain.model;

import com.fifteen.eureka.common.auditor.BaseEntity;
import com.fifteen.eureka.common.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_user")
@SQLDelete(sql = "UPDATE p_user SET is_deleted = true, approval_status = 'REJECTED' WHERE id = ?")
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(length = 100)
  private String slackId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

  @Column(nullable = false)
  private boolean isDeleted = false;

  public User(String username, String password, String email, String slackId, Role role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.slackId = slackId;
    this.role = role;
  }

  public void updateApprovalStatus(ApprovalStatus approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public void updateUserInfo(String email, String slackId, Role role, String password,
      PasswordEncoder passwordEncoder) {
    if (email != null && !email.isBlank()) {
      this.email = email;
    }
    if (slackId != null && !slackId.isBlank()) {
      this.slackId = slackId;
    }
    if (role != null) {
      this.role = role;
    }
    if (password != null && !password.isBlank()) {
      this.password = passwordEncoder.encode(password);
    }
  }

}
