package com.easy_p.easyp.common.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class AbstractRedisStore implements RedisStore{
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void store(String keyData, String data) {
        String key = genKey(keyData);
        long expiration = getExpiration();
        redisTemplate.opsForValue().set(key, data, expiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public String get(String keyData) {
        String key = genKey(keyData);
        return redisTemplate.opsForValue().get(key);
    }
}
