package com.fifteen.eureka.user.infrastructure.config;

import com.fifteen.eureka.user.infrastructure.security.AuthenticationFilter;
import com.fifteen.eureka.user.infrastructure.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final AuthenticationFilter authenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(FilterConfig.EXCLUDED_PATHS.toArray(new String[0])).permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 실패 핸들러
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 무상태 세션
        )
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class); // 커스텀 필터 추가
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 비밀번호 암호화용
  }

  // 사용자 인증 처리 : 성공 - 인증 정보 반환 / 실패 - 예외처리
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}