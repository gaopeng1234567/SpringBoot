package com.patrick.redis.service;

import com.patrick.redis.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author patrick
 * @date 2021/3/26 3:37 下午
 * @Des redis
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Service
@SuppressWarnings("all")
public class StringRedisService {
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

    // 添加key-value
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 追加添加key-value
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    // 使用key查询value并修改value值，redis会保证原子性
    public void getAndSet(String key, String value) {
        redisTemplate.opsForValue().getAndSet(key, value);
    }

    // 添加key-value，当key存在的时候才生效，会覆盖旧值
    public Boolean setIfPresent(String key, String value) {
        return redisTemplate.opsForValue().setIfPresent(key, value);
    }

    // 添加key-value，当key不存在的时候才生效
    public Boolean setIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    // 使用key查询value的长度
    public Long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    // 查询多个key，根据key的顺序返回值，没查询到的值会返回null，不去重
    public List<String> multiGet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    // 批量添加多个key-value
    public void multiSet(Map<String, String> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    // 批量添加多个key-value，key不存在的时候才生效，注意必须所有key都不存在才成功，否则全部失败
    public Boolean multiSetIfAbsent(Map<String, String> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    // 原子递增，如果key不存在则直接创建一个
    public Long increment(String key, Long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }
}
