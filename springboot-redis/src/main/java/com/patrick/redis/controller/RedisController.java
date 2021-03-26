package com.patrick.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.interfaces.PBEKey;

/**
 * @author patrick
 * @date 2021/3/26 10:53 上午
 * @Des redis
 * 最簡單的事是堅持，最難的事還是堅持
 */
@RestController
public class RedisController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/redis")
    public String getRedisValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

}
