package com.easy_p.easyp.common.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenStore extends AbstractRedisStore{
    @Value("${jwt.refresh-token-expiration}")
    private int expiration;

    public RefreshTokenStore(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    public void store(String email, String refreshToken){
        super.store(email, refreshToken);
    }

    public String get(String email){
        return super.get(email);
    }

    @Override
    public String genKey(String keyData) {
        return "refresh:" + keyData;
    }

    @Override
    public long getExpiration() {
        return expiration;
    }
}
