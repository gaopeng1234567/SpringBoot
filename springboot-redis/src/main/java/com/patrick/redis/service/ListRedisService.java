package com.patrick.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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

    //    ***********************************应用场景***********************************

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

    //    ***********************************API使用***********************************
    // 向左头部插入一个数据,返回集合长度
    public Long leftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    // 从集合尾部插入元素，自动创建集合，返回集合长度
    public Long rightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    // 从集合头部批量插入元素，自动创建集合，返回集合长度
    public Long leftPushAll(String key, String... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    // 从集合头部批量元素，不自动创建集合，返回集合长度，集合不存在返回0
    public Long leftPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    // 获取集合指定区间元素，集合不存在返回空集合，超出集合范围不返回任何元素
    public List<String> range(String key, Long start, Long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    // 从指定集合的头部弹出元素，集合不存在则返回null
    public String leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key).toString();
    }

    // 从指定集合的头部弹出元素，集合不存在会一直阻塞等待，超时则返回null
    public void leftPop2(String key, Long time) {
        redisTemplate.opsForList().leftPop(key, time, TimeUnit.SECONDS);
    }

    // 从指定集合的指定索引获取元素，集合不存在则返回null，索引可以输入负数，从尾部开始计算
    public void index(String key, Long index) {
        redisTemplate.opsForList().index(key, index);
    }

    // 从指定集合的指定索引位置插入元素，集合不存在或索引越界会抛出异常，索引可以输入负数，从尾部开始计算
    public void set(String key, Long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    // 从指定集合的指定索引位置移除指定元素，集合不存在或索引越界会返回0，value值匹配上才会删除，索引可以输入负数，从尾部开始计算
    public Long remove(String key, Long index, String value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }

    // 从指定集合移除指定区间元素")
    public void trim(String key, Long start, Long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    // 获取指定集合长度
    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // 从sourceKey尾部集合弹出一个元素，放入destinationKey集合的头部，如果目标集合不存在则会创建一个
    public void rightPopAndLeftPush(String sourceKey, String destinationKey) {
        redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    // 从sourceKey尾部集合弹出一个元素，放入destinationKey集合的头部，如果目标集合不存在则会创建一个，如果源集合不存在则会一直阻塞等待，直到超时为止
    public void rightPopAndLeftPush2(String sourceKey, String destinationKey, Long time) {
        redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, time, TimeUnit.SECONDS);
    }
}
