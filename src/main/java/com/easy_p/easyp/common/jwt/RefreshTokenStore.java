package com.easy_p.easyp.common.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenStore {
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${jwt.refresh-token-expiration}")
    private int expiration;
    public void storeRefreshToken(String email, String refreshToken){
        redisTemplate.opsForValue().set(email, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String email){
        return redisTemplate.opsForValue().get(email);
    }
}
