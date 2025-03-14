package com.easy_p.easyp.common.store;

public interface RedisStore {
    void store(String keyData, String data);
    String get(String key);
    void delete(String keyData);
    String genKey(String keyData);
    long getExpiration();
}
