package com.patrick.redis.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author patrick
 * @date 2021/3/31 2:00 下午
 * @Des 测试redis锁
 * 最簡單的事是堅持，最難的事還是堅持
 */
@RestController
public class RedisLockController {

    @Value("${server.port}")
    private String port;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Redisson redisson;


    @GetMapping("/lock")
    public String redisLockController() {
        //在分布式场景下会失效
        synchronized (this) {
            Integer count = Integer.parseInt(stringRedisTemplate.opsForValue().get("patrick"));
            if (count > 0) {
                int realCount = count - 1;
                stringRedisTemplate.opsForValue().set("patrick", realCount + "");
                System.out.println(String.format("扣件成功:剩余%s", realCount));
            } else {
                System.out.println("商品不足");
            }
        }
        return port;
    }

    @GetMapping("/lock1")
    public String redisLockController2() {
        String uuid = UUID.randomUUID().toString();
        //基于redis setnx特性进行上锁
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 10, TimeUnit.SECONDS);
        if (!lock) {
            // 锁已被占用
            return "火爆、稍等片刻";
        }
        try {
            Integer count = Integer.parseInt(stringRedisTemplate.opsForValue().get("patrick"));
            if (count > 0) {
                int realCount = count - 1;
                stringRedisTemplate.opsForValue().set("patrick", realCount + "");
                System.out.println(String.format("扣件成功:剩余%s", realCount));
            } else {
                System.out.println("商品不足");
            }
        } finally {
            String lock1 = stringRedisTemplate.opsForValue().get("lock");
            if (lock1 == uuid) {
                stringRedisTemplate.delete("lock");
            }
        }
        return port;
    }

    @GetMapping("/lock1")
    public String redisLockController3() {
        // redisson 创建RLock不是去redis 获取patrick，在lock的时候用这个参数
        RLock patrick = redisson.getLock("patrick");
        try {
            // redisson上锁
            patrick.lock();
            Integer count = Integer.parseInt(stringRedisTemplate.opsForValue().get("patrick"));
            if (count > 0) {
                int realCount = count - 1;
                stringRedisTemplate.opsForValue().set("patrick", realCount + "");
                System.out.println(String.format("扣件成功:剩余%s", realCount));
            } else {
                System.out.println("商品不足");
            }
        } finally {
            // redisson 解锁
            patrick.unlock();
        }
        return port;
    }
}
