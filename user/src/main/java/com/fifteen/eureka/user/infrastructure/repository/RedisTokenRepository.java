package com.fifteen.eureka.user.infrastructure.repository;

import com.fifteen.eureka.user.domain.repository.AuthRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository implements AuthRepository {

  private final RedisTemplate<String, String> redisTemplate;

  @Value("${jwt.refresh.expiration}")
  private long refreshTokenValidity;

  @Override
  public boolean saveRefreshToken(Long userId, String refreshToken) {
    long remainingExpiration = refreshTokenValidity * 1000L;

    if (remainingExpiration <= 0) {
      throw new IllegalArgumentException("Expiration time must be greater than zero");
    }

    String refreshKey = getKey("refreshToken", refreshToken);
    String userKey = getKey("userId", userId.toString());

    redisTemplate.opsForValue().set(refreshKey, userId.toString(), remainingExpiration, TimeUnit.MILLISECONDS);
    redisTemplate.opsForValue().set(userKey, refreshToken, remainingExpiration, TimeUnit.MILLISECONDS);

    return redisTemplate.hasKey(refreshKey) && redisTemplate.hasKey(userKey);
  }

  @Override
  public boolean deleteRefreshToken(Long userId) {
    String userKey = getKey("userId", userId.toString());
    String refreshToken = redisTemplate.opsForValue().get(userKey);

    if (refreshToken != null) {
      String refreshKey = getKey("refreshToken", refreshToken);
      boolean refreshDeleted = Boolean.TRUE.equals(redisTemplate.delete(refreshKey));
      boolean userDeleted = Boolean.TRUE.equals(redisTemplate.delete(userKey));
      return refreshDeleted && userDeleted; // 두 키 모두 삭제되었을 경우 true 반환
    }
    return false;
  }

  // 액세스 토큰 블랙리스트 추가
  public boolean addToBlacklist(String accessToken, long remainingExpiration) {
    String blacklistKey = "blacklist:" + accessToken;

    redisTemplate.opsForValue().set(
        blacklistKey,
        "blacklisted",
        remainingExpiration, // TTL 설정 (밀리초 단위)
        TimeUnit.MILLISECONDS
    );

    // 저장된 키 확인
    return redisTemplate.hasKey(blacklistKey);
  }

  @Override
  public boolean isBlacklisted(String accessToken) {
    return redisTemplate.hasKey("blacklist:" + accessToken);
  }

  // 키 구조
  private String getKey(String type, String token) {
    return type + ":" + token;
  }
}
