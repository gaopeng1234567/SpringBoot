package com.patrick.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author patrick
 * @date 2021/3/27 12:05 下午
 * @Des list redis
 * 最簡單的事是堅持，最難的事還是堅持
 */

@Service
@SuppressWarnings({"all"})
public class ListRedisService {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 实现栈的的数据结构 先进后厨 FILO
     * LPUSH + LPOP
     */
    public void stackListRedisMethod() {
        redisTemplate.opsForList().leftPush("fdev", "patrick");
        redisTemplate.opsForList().leftPush("fdev", "zhangSan");
        redisTemplate.opsForList().leftPush("fdev", "liSi");

        System.out.println(redisTemplate.opsForList().leftPop("fdev"));
        System.out.println(redisTemplate.opsForList().leftPop("fdev"));
        System.out.println(redisTemplate.opsForList().leftPop("fdev"));
    }

    /**
     * 实现队列的数据结构 先进先出 FIFO
     * Queue(队列）= LPUSH + RPOP
     */
    public void queueListRedisMethod() {
        redisTemplate.opsForList().leftPush("fdev", "patrick");
        redisTemplate.opsForList().leftPush("fdev", "zhangSan");
        redisTemplate.opsForList().leftPush("fdev", "liSi");

        System.out.println(redisTemplate.opsForList().rightPop("fdev"));
        System.out.println(redisTemplate.opsForList().rightPop("fdev"));
        System.out.println(redisTemplate.opsForList().rightPop("fdev"));
    }

    /**
     * 实现阻塞队列的数据结构 先进先出 FIFO
     * Blocking MQ(阻塞队列）= LPUSH + BRPOP
     * <p>
     * timeout = 0 一直阻塞等待
     */
    public void blockQueueListRedisMethod() {
        redisTemplate.opsForList().leftPush("fdev", "patrick");
        redisTemplate.opsForList().leftPush("fdev", "zhangSan");
        redisTemplate.opsForList().leftPush("fdev", "liSi");

        System.out.println(redisTemplate.opsForList().rightPop("fdev", 20, TimeUnit.SECONDS));
        System.out.println(redisTemplate.opsForList().rightPop("fdev", 20, TimeUnit.SECONDS));
        System.out.println(redisTemplate.opsForList().rightPop("fdev", 20, TimeUnit.SECONDS));
    }

    /**
     * 微博、公众号等消息拉去 设计（适合小博主）
     * 关注的博主，他们发送推文的时候，先将次推文id放入 我的redis中 LPUSH
     * key 我的id value 消息id
     * 然后每次拉取 LRANGE 0 5
     * <p>
     * 如果是大博主，几百万几千万粉丝，则采用 pull方式（不一定是redis了）大V维护一个自己的队列，所有用户去这里面去拉取
     * 缺点: 关注了很多大V,那么要复杂点，换要进行排序
     */
    public void messageListRedisMethod() {
        redisTemplate.opsForList().range("myUserId", 0, 4);
    }
}
