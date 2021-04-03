package com.patrick.redis.service;

import com.patrick.redis.model.ProductSku;
import com.patrick.redis.model.User;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author patrick
 * @date 2021/3/27 9:50 上午
 * @Des hash redis
 * 最簡單的事是堅持，最難的事還是堅持
 */

@Service
@SuppressWarnings({"all"})
public class HashRedisService {

    private static final String CART_KEY = "cart:";

    @Resource
    private RedisTemplate redisTemplate;


    //    ***********************************应用场景***********************************

    /**
     * 电商购物车
     * 1）以用户id为key
     * 2）商品id为field
     * 3）商品数量为value
     */
    public void cartHashRedis() {
        //用户
        User patrick = User.builder().userId(1001).age(18).userName("patrick").build();
        //商品
        ProductSku productSku = ProductSku.builder().skuName("3080显卡").skuId(001).skuPrice(9000.00).totalNum(1).build();
        redisTemplate.opsForHash().put(CART_KEY + patrick.getUserId().toString(), productSku.getSkuId(), productSku.getTotalNum());
        System.out.println(redisTemplate.opsForHash().get(CART_KEY + patrick.getUserId(), productSku.getSkuId()));
    }

    /**
     * 购物车操作
     * <p>
     * 添加商品 hset cart:1001 10088 1
     * 增加数量 hincrby cart:1001 10088 1
     * 商品总数 hlen cart:1001
     * 删除商品 hdel cart:1001 10088
     * 获取购物车所有商品hgetall cart:1001
     */
    public void operateCartHashRedis() {
        //添加商品
        User patrick = User.builder().userId(1001).age(18).userName("zhangsan").build();
        ProductSku productSku = ProductSku.builder().skuName("3080显卡").skuId(002).skuPrice(9000.00).totalNum(1).build();
        redisTemplate.opsForHash().put(CART_KEY + patrick.getUserId().toString(), productSku.getSkuId(), productSku.getTotalNum());

        //增加数量
        redisTemplate.opsForHash().increment(CART_KEY + patrick.getUserId(), productSku.getSkuId(), productSku.getTotalNum());
        System.out.println(String.format("增加商品后数量为:%s",
                redisTemplate.opsForHash().get(CART_KEY + patrick.getUserId(), productSku.getSkuId())));

        //商品总数
        Long size = redisTemplate.opsForHash().size(CART_KEY + patrick.getUserId());
        System.out.println(String.format("当前商品:%s,总数为:%s", productSku.getSkuId(), size));

        //删除商品
        Long delete = redisTemplate.opsForHash().delete(CART_KEY + patrick.getUserId(), productSku.getSkuId());
        System.out.println(String.format("当前商品:%s,总数为:%s", productSku.getSkuId(), delete));

        //获取购物车所有商品
        Map entries = redisTemplate.opsForHash().entries(CART_KEY + patrick.getUserId());
        System.out.println(String.format("当前用户:%s,总数为:%s", patrick.getUserId(), entries.size()));
    }

    //    ***********************************API使用***********************************
    // 往hash类型key中添加数据
    public void put(String hashKey, String key, String value) {
        redisTemplate.opsForHash().put(hashKey, key, value);
    }

    // 根据key获取value
    public void get(String hashKey, String key) {
        redisTemplate.opsForHash().get(hashKey, key);
    }

    // 根据key批量删除value，返回删除数量
    public Long delete(String hashKey, String... key) {
        return redisTemplate.opsForHash().delete(hashKey, (Object[]) key);
    }

    // 查询hash中的元素数量
    public Long size(String hashKey) {
        return redisTemplate.opsForHash().size(hashKey);
    }

    // 查询hash中的value的长度
    public Long lengthOfValue(String hashKey, String key) {
        return redisTemplate.opsForHash().lengthOfValue(hashKey, key);
    }

    //获取整个hash集合
    public Map<String, String> entries(String hashKey) {
        return redisTemplate.opsForHash().entries(hashKey);
    }

    // 判断hash中是否存在指定的key
    public Boolean hasKey(String hashKey, String key) {
        return redisTemplate.opsForHash().hasKey(hashKey, key);
    }

    // 创建hash类型的数据，当hash中不存在该key-value时才成功
    public Boolean putIfAbsent(String hashKey, String key, String value) {
        return redisTemplate.opsForHash().putIfAbsent(hashKey, key, value);
    }

    // 获取key的集合
    public Set<String> keys(String hashKey) {
        return redisTemplate.opsForHash().keys(hashKey);
    }

    // 获取value的集合
    public List<String> values(String hashKey) {
        return redisTemplate.opsForHash().values(hashKey);
    }

    // 批量获取value，不存在的key返回null，即使所有key都不存在也会返回一个全是null的集合
    public List<String> multiGet(String hashKey, List<String> keyList) {
        return redisTemplate.opsForHash().multiGet(hashKey, keyList);
    }

    // 往hash类型key中批量添加数据
    public void putAll(String hashKey, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(hashKey, map);
    }

    // 根据key对value进行自增，返回自增后的结果
    public Long increment(String hashKey, String key, Long nums) {
        return redisTemplate.opsForHash().increment(hashKey, key, nums);
    }

    // 遍历hash
    public void scan(String hashKey) {
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        Cursor<Map.Entry<String, String>> cursor = redisTemplate.opsForHash().scan(hashKey, scanOptions);
        cursor.forEachRemaining(e -> {
            System.out.println(e.getKey() + ":" + e.getValue());
        });
    }
}
