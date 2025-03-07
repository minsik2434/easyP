package com.easy_p.easyp.common.store;

public interface RedisStore {
    void store(String keyData, String data);
    String get(String key);
    String genKey(String keyData);
    long getExpiration();
}
