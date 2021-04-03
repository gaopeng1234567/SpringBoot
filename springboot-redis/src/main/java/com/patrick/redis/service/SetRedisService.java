package com.patrick.redis.service;

import com.patrick.redis.model.User;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author patrick
 * @date 2021/3/27 9:50 上午
 * @Des set redis
 * 最簡單的事是堅持，最難的事還是堅持
 */

@Service
@SuppressWarnings({"all"})
public class SetRedisService {

    private static final String DROW = "chouJiang"; //不要问为啥这是拼音
    private static final String ThumbsUp = "dianzan";
    @Resource
    private RedisTemplate redisTemplate;
    //    ***********************************应用场景***********************************

    /**
     * 实现抽奖 功能
     * 1、点击参与加入抽奖 SADD key {userlD}
     * 2、查看参与抽奖用户 SMEMBERS key
     * 3、输入中奖人数、随机返回（可放回、可不放回）SRANDMEMBER key [count] / SPOP key [count]
     */
    public void luckDrawSetRedisMethed() {
        // 点击参与抽奖
        redisTemplate.opsForSet().add(DROW, User.builder().userId(001).build());
        redisTemplate.opsForSet().add(DROW, User.builder().userId(002).build());
        redisTemplate.opsForSet().add(DROW, User.builder().userId(003).build());
        redisTemplate.opsForSet().add(DROW, User.builder().userId(004).build());

        // 查看抽奖人数
        Set members = redisTemplate.opsForSet().members(DROW);
        members.forEach(System.out::println);

        // 抽奖
        List list = redisTemplate.opsForSet().randomMembers(DROW, 2); //抽中的还能被抽中

        Set set = redisTemplate.opsForSet().distinctRandomMembers(DROW, 2);//抽中的不能被抽中

        List pop = redisTemplate.opsForSet().pop(DROW, 2);//抽中不放回,且清空了，简单多了，垃圾笨鸟中奖中不了
        list.forEach(System.out::println);
        System.out.println("----------------------------");
        set.forEach(System.out::println);
        System.out.println("----------------------------");
        pop.forEach(System.out::println);

    }


    /**
     * 点赞，关注、收藏场景
     * 1）点赞
     * SADD  like:{消息ID}  {用户ID}
     * 2) 取消点赞
     * SREM like:{消息ID}  {用户ID}
     * 3) 检查用户是否点过赞  是否在这条文章中高亮自己
     * SISMEMBER  like:{消息ID}  {用户ID}
     * 4) 获取点赞的用户列表
     * SMEMBERS like:{消息ID}
     * 5) 获取点赞用户数
     * SCARD like:{消息ID}
     */
    public void giveTheThumbsUpSetRedisMethod() {
        // 点赞
        redisTemplate.opsForSet().add(ThumbsUp, User.builder().userId(001).build().getUserId());
        redisTemplate.opsForSet().add(ThumbsUp, User.builder().userId(8).build().getUserId());

        // 取消点赞
        redisTemplate.opsForSet().remove(ThumbsUp, User.builder().userId(001).build().getUserId());

        // 检查用户是否点过赞
        redisTemplate.opsForSet().isMember(ThumbsUp, User.builder().userId(001).build().getUserId());

        // 获取点赞的用户列表
        Set members = redisTemplate.opsForSet().members(ThumbsUp);
        members.forEach(System.out::println);

        // 获取点赞用户数
        Long size = redisTemplate.opsForSet().size(ThumbsUp);
        System.out.println(size);
    }

    /**
     * set 高阶集合操作
     * SINTER  key  [key ...] 				        //交集运算   共同关注
     * set1 = 1,2,3 set2 = 3,5,7 set3 = 1,3,7  set = 3
     * SINTERSTORE  destination  key  [key ..]		//将交集结果存入新集合destination中
     * set1 = 1,2,3 set2 = 3,5,7 set3 = 1,3,7  set = 3
     * SUNION  key  [key ..] 				        //并集运算   一起关注
     * set1 = 1,2,3 set2 = 3,5,7 set3 = 1,3,7  set = 1,2,3,5,6
     * SUNIONSTORE  destination  key  [key ...]		//将并集结果存入新集合destination中
     * SDIFF  key  [key ...] 				        //差集运算   可能认识的人
     * 以第一个集合为基准-后面所有集合的并集
     * SDIFFSTORE  destination  key  [key ...]		//将差集结果存入新集合destination中
     * <p>
     * 实现微博等社交app的 关注模型
     * 举例: 我关注的人 也关注了他
     * 我关注的人 他也关注了
     */
    public void aggregateOpsSetRedisMethod() {

    }

    //    ***********************************API使用***********************************
    // 批量插入元素，自动创建集合，返回集合长度
    public Long add(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    // 获取集合中的全部元素，集合不存在返回空集合
    public Set<String> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // 随机获取集合中的指定数量的元素，集合不存在返回null
    public void pop(String key) {
        redisTemplate.opsForSet().pop(key);
    }

    // 随机获取集合中的指定数量的元素，集合不存在返回空集合
    public List<String> pop2(String key, Long nums) {
        return redisTemplate.opsForSet().pop(key, nums);
    }

    // 判断集合中是否包含指定元素，集合不存在也返回false
    public Boolean isMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    // 移除集合中的指定元素集，返回移除数量，集合不存在则返回0
    public Long remove(String key, String... values) {
        return redisTemplate.opsForSet().remove(key, (Object) values);
    }

    // 随机获取集合中的一个元素，但是元素不会被移除，集合为空返回null
    public void randomMember(String key) {
        redisTemplate.opsForSet().randomMember(key);
    }

    // 求两个集合的交集
    public Set<String> intersect(String key1, String key2) {
        return redisTemplate.opsForSet().intersect(key1, key2);
    }

    // 求两个集合的交集，并将结果存储在一个新key之中
    public Long intersectAndStore(String key1, String key2, String newKey) {
        return redisTemplate.opsForSet().intersectAndStore(key1, key2, newKey);
    }

    // 求两个集合的差集
    public Set<String> difference(String key1, String key2) {
        return redisTemplate.opsForSet().difference(key1, key2);
    }

    // 求两个集合的并集
    public Set<String> union(String key1, String key2) {
        return redisTemplate.opsForSet().union(key1, key2);
    }

    // 从集合中获取指定数量的随机元素，并保证元素不重复
    public Set<String> distinctRandomMembers(String key, Long nums) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, nums);
    }

    // 遍历Set集合
    public void scan(String key) throws IOException {
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        try (Cursor<String> cursor = redisTemplate.opsForSet().scan(key, scanOptions)) {
            cursor.forEachRemaining(System.out::println);
        }
    }
}
