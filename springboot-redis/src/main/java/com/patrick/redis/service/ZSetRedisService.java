package com.patrick.redis.service;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
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

    //    ***********************************应用场景***********************************

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

    //    ***********************************API使用***********************************

    // 往ZSet集合中添加元素，并设置次数，如果存在则更新次数，不存在则新增，只有新增操作时才返回trueredisTemplate.opsForZSet()
    public Boolean add(String key, String value, Double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    // 根据次数区间统计元素数量redisTemplate.opsForZSet()
    public Long count(String key, Double min, Double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    // 获取指定元素的次数redisTemplate.opsForZSet()
    public Double score(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    // 遍历ZSet集合redisTemplate.opsForZSet()
    public void scan(String key) {
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        Cursor<ZSetOperations.TypedTuple<String>> cursor = redisTemplate.opsForZSet().scan(key, scanOptions);
        cursor.forEachRemaining(e -> {
            System.out.println(e.getValue() + ":" + e.getScore());
        });
    }

    // 递增元素的次数，返回递增后的结果
    public Double incrementScore(String key, String value, Double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    // 获取当前集合大小
    public Long size(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    // 获取当前集合key的数量
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    // 根据元素的降序升序位置获得当前元素的索引
    public Long reverseRank(String key, String value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    // 求两个集合的并集，并且存入一个新的集合中，将相同元素的次数进行相加，返回并集数量
    public Long unionAndStore(String key1, String key2, String newKey) {
        return redisTemplate.opsForZSet().unionAndStore(key1, key2, newKey);
    }

    // 求多个集合的并集，并且存入一个新的集合中，将相同元素的次数进行相加，返回并集数量，支持聚合函数设置
    public Long unionAndStore2(String key, List<String> list, String newKey) {
        // list参数是存放key的，如果合并多个key则都把key存放在list中
        // RedisZSetCommands.Aggregate 是指定合并的时候对相同元素的合并方式，MIN表示取最小值，MAX取最大值，SUM求和
        return redisTemplate.opsForZSet().unionAndStore(key, list, newKey, RedisZSetCommands.Aggregate.MIN);
    }

    // 求多个集合的并集，并且存入一个新的集合中，将相同元素的次数进行相加，返回并集数量，支持乘法系数和聚合函数设置
    public Long unionAndStore3(String key, List<String> list, String newKey) {
        // list参数是存放key的，如果合并多个key则都把key存放在list中
        // RedisZSetCommands.Aggregate 是指定合并的时候对相同元素的合并方式，MIN表示取最小值，MAX取最大值，SUM求和
        // RedisZSetCommands.Weights 是乘以的系数，每个元素都会乘以这个系数，需要注意的是list中有多少个集合，就必须要有几个系数
        return redisTemplate.opsForZSet().unionAndStore(key, list, newKey, RedisZSetCommands.Aggregate.SUM, RedisZSetCommands.Weights.of(3, 2));
    }

    // 求两个集合的交集，并把结果存入新的集合，返回心机和的数量，此类方法有和unionAndStore一样的重载方法
    public Long intersectAndStore(String key1, String key2, String newKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key1, key2, newKey);
    }

    // 获取指定元素在ZSet集合中的索引位置，按value升序顺序，reverseRank按value降序顺序
    public Long rank(String key, String value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    // 根据value升序顺序进行区间筛选，reverseRange方法是根据降序顺序进行筛选
    public Set<String> range(String key, Long start, Long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    // 按字典顺序获取集合所有的key值，reverseRangeByLex是按照反序筛选
    public Set<String> rangeByLex(String key) {
        // 默认范围是整个集合最小值和最大值
        // RedisZSetCommands.Range.range().lte(10).gte(5)，也可以使用连缀方式限定范围
        // gt: greater than 大于
        // gte: greater than or equal 大于等于
        // lt: less than 小于
        // lte: less than or equal 小于等于
        return redisTemplate.opsForZSet().rangeByLex(key, RedisZSetCommands.Range.range());
    }

    // 按字典获取集合所有的key值，设置取值的个数，以及开始取值的索引位置，reverseRangeByLex是按照反序筛选
    public Set<String> rangeByLex2(String key) {
        // RedisZSetCommands.Limit.limit(); 这是默认方式，不做任何限制
        // RedisZSetCommands.Limit.limit().count(1).offset(1); 使用连缀方式设置限制，count表示取一个，offset表示从索引1开始取
        return redisTemplate.opsForZSet().rangeByLex(key, RedisZSetCommands.Range.range(), RedisZSetCommands.Limit.limit().count(1).offset(1));
    }

    // 按照次数升序顺序的区间范围获取元素，reverseRangeByScore是按照降序
    public Set<String> rangeByScore(String key, Double start, Double end) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    // 按照次数区间范围获取元素，可以设置取值个数(count)，开始取值的索引(offset)，reverseRangeByScore是按照降序
    public Set<String> rangeByScore2(String key, Double start, Double end
            , Long offset, Long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end, offset, count);
    }

    // 根据次数升序顺序进行区间筛选，返回一个Tuple类型的集合，也就是把value和score都返回，reverseRangeByScoreWithScores是降序
    public Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores(String key, Long start, Long end) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, start, end);
    }

    // 根据次数升序顺序进行区间筛选，返回一个Tuple类型的集合，
    // 也就是把value和score都返回，" +"可以设置取值个数(count)，开始取值的索引(offset)，reverseRangeByScoreWithScores是降序
    public Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores2(String key, Long start, Long end
            , Long offset, Long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, start, end, offset, count);
    }
}
