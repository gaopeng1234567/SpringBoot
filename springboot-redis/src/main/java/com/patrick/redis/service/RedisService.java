package com.patrick.redis.service;

import com.patrick.redis.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick
 * @date 2021/3/26 3:37 下午
 * @Des redis
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Service
public class RedisService {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 单值缓存
     */
    public void setSingleValueRedis() {
        redisTemplate.opsForValue().set("patrick", "666");
        System.out.println(redisTemplate.opsForValue().get("patrick"));
    }

    /**
     * 多值缓存
     */
    public void setMultiValueRedis() {
        Map<String, String> map = new HashMap<>();
        map.put("patrick", "666"); // key value
        map.put("zhangMan", "555");
        map.put("zhangSan", "444");
        redisTemplate.opsForValue().multiSet(map);
        System.out.println(redisTemplate.opsForValue().get("patrick"));
        System.out.println(redisTemplate.opsForValue().get("zhangMan"));
        System.out.println(redisTemplate.opsForValue().get("zhangSan"));
    }

    /**
     * 对象缓存
     */
    public void setObjectValueRedis() {
        User patrick = User.builder().userId(11).age(18).userName("patrick").build();
        redisTemplate.opsForValue().set("patrick", patrick);
        System.out.println(redisTemplate.opsForValue().get("patrick"));
    }

    /**
     * 分布式锁
     */
    public void distributedLockRedis() {

    }

    /**
     * 计数器（阅读文章计数器等）
     */
    public void readArticleCountRedis() {
        redisTemplate.opsForValue().increment("articleReadCount");
        System.out.println(redisTemplate.opsForValue().get("articleReadCount"));
    }
}
