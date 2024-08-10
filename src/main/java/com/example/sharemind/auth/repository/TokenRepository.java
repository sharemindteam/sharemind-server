package com.example.sharemind.auth.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class TokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, String value, Duration duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    public String findByKey(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }
}
