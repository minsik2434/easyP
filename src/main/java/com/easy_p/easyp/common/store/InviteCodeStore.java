package com.easy_p.easyp.common.store;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class InviteCodeStore extends AbstractRedisStore{
    public InviteCodeStore(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    public void store(Long projectId, String inviteeEmail, String inviteCode){
        String key = projectId + "-" + inviteeEmail;
        super.store(key, inviteCode);
    }

    public String get(Long projectId, String inviteeEmail){
        String key = projectId +"-" + inviteeEmail;
        return super.get(key);
    }

    public void delete(Long projectId, String inviteeEmail){
        String key = projectId + "-" + inviteeEmail;
        super.delete(key);
    }

    @Override
    public String genKey(String keyData) {
        return "inviteCode:" + keyData;
    }

    @Override
    public long getExpiration() {
        return 60000;
    }
}
