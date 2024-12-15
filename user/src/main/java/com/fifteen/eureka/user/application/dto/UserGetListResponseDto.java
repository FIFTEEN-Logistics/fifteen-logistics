package com.fifteen.eureka.user.application.dto;

import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.user.domain.model.ApprovalStatus;
import com.fifteen.eureka.user.domain.model.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

@Getter
@Builder
@AllArgsConstructor
public class UserGetListResponseDto {

  private UserPage userPage;

  public static UserGetListResponseDto of(Page<User> userPage) {

    return UserGetListResponseDto.builder()
            .userPage(new UserPage(userPage))
            .build();
  }

  @Getter
  @ToString
  public static class UserPage extends PagedModel<UserPage.User> {

    public UserPage(Page<com.fifteen.eureka.user.domain.model.User> userPage) {
      super(
              new PageImpl<>(
                      User.from(userPage.getContent()),
                      userPage.getPageable(),
                      userPage.getTotalElements()
              )
      );
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class User {
      private Long userId;
      private String username;
      private String email;
      private String slackId;
      private Role role;
      private ApprovalStatus approvalStatus;

      public static List<User> from(List<com.fifteen.eureka.user.domain.model.User> userList) {
        return userList.stream()
                .map(User::from)
                .toList();
      }

        public static User from(com.fifteen.eureka.user.domain.model.User user) {
        return User.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .slackId(user.getSlackId())
                .role(user.getRole())
                .approvalStatus(user.getApprovalStatus())
                .build();
      }
    }
  }
}
