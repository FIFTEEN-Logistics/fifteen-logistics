package com.fifteen.eureka.gateway.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository {

  private final RedisTemplate<String, String> redisTemplate;

  // 리프레시 토큰 검증
  public boolean isRefreshTokenValid(String refreshToken) {
    String refreshKey = getKey("refreshToken", refreshToken);
    return redisTemplate.hasKey(refreshKey);
  }

  // 액세스 토큰 블랙리스트 검증
  public boolean isBlacklisted(String accessToken) {
    return redisTemplate.hasKey("blacklist:" + accessToken);
  }

  // 키 구조
  private String getKey(String type, String value) {
    return type + ":" + value;
  }
}
