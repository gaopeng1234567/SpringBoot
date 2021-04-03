package com.patrick.redis.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author patrick
 * @date 2021/4/3 1:23 下午
 * @Des pipeline用法
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Service
public class PipeLineRedisService {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 多个命令发到管道一起执行,按顺序返回执行结果
     *
     * @param key
     * @param value
     */
    public void executePipelined(String key, String value) {
        List<Object> list = redisTemplate.executePipelined((RedisCallback<String>) redisConnection -> {
            StringRedisConnection connection = (StringRedisConnection) redisConnection;
            connection.set(key, value);
            connection.incr(key);
            connection.get(key);
            return null;
        });
        System.out.println(list.toString());
    }

    /**
     * 多个命令发到管道一起执行,按顺序返回执行结果
     *
     * @param key
     * @param value
     */
    public void execute(String key, String value) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                redisOperations.boundValueOps((K) key).set((V) value);
                return null;
            }
        });
        Object execute = redisTemplate.execute
                ((RedisCallback<Map<byte[], byte[]>>) con -> con.hGetAll(key.getBytes()));

    }
}
