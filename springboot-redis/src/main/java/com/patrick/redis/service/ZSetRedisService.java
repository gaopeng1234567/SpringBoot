package com.patrick.redis.service;

import com.patrick.redis.model.ProductSku;
import com.patrick.redis.model.User;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author patrick
 * @date 2021/3/27 9:50 上午
 * @Des zset redis
 * 最簡單的事是堅持，最難的事還是堅持
 */

@Service
@SuppressWarnings({"all"})
public class ZSetRedisService {

    private static final String HotNews = "hotNews";

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 热点新闻排行榜
     * 1）点击新闻
     * ZINCRBY  hotNews:001  1  杨幂嫁给了patrick
     * 2）展示当日排行前十
     * ZREVRANGE  hotNews:002  0  9  WITHSCORES
     * 3）七日搜索榜单计算
     * ZUNIONSTORE  hotNews:001-007  7
     * hotNews:001  hotNews:001... hotNews:007
     * 4）展示七日排行前十
     * ZREVRANGE hotNews:001-007  0  9  WITHSCORES
     */
    public void hotNewSortZsetRedisMethod() {
        // 添加一个热点新闻
        redisTemplate.opsForZSet().add(HotNews + "004", "杨幂嫁给了patrick", 20);
        redisTemplate.opsForZSet().add(HotNews + "005", "杨幂嫁给了patrick", 60);
        redisTemplate.opsForZSet().add(HotNews + "006", "杨幂嫁给了patrick", 20);
        // 对杨幂嫁给了patrick 的分数加1
        Double patrick = redisTemplate.opsForZSet().incrementScore(HotNews + "001", "杨幂嫁给了patrick", 1);
        System.out.println(String.format("当前权值: %s", patrick));

        // Get elements in range from start to end from sorted set ordered from high to low english牛逼不？
        Set range = redisTemplate.opsForZSet().reverseRange(HotNews + "001", 0, 20);
        range.forEach(System.out::println);
        //七日排行统计 Union sorted sets at key and otherKeys and store result in destination destKey
        redisTemplate.opsForZSet().unionAndStore("hotNews001", Arrays.asList(HotNews + "004", HotNews + "005", HotNews + "006"), "hotNews:001-007");
        // 获取hotNews:001-007中的前10个热点
        Set set = redisTemplate.opsForZSet().reverseRange("hotNews:001-007", 0, 9);
        set.forEach(System.out::println);

    }
}
