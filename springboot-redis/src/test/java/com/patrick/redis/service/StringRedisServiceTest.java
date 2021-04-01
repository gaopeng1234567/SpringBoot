package com.patrick.redis.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author patrick
 * @date 2021/3/26 3:41 下午
 * @Des 最簡單的事是堅持，最難的事還是堅持
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("single-redis")
public class StringRedisServiceTest {
    @Autowired
    private StringRedisService redisService;

    @Test
    public void fun_1() {
        redisService.setSingleValueRedis();
    }

    @Test
    public void fun_2() {
        redisService.setMultiValueRedis();
    }

    @Test
    public void fun_3() {
        redisService.setObjectValueRedis();
    }

    @Test
    public void fun_4() {
        redisService.readArticleCountRedis();
    }
}