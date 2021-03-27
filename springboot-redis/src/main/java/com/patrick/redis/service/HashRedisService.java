package com.patrick.redis.service;

import com.patrick.redis.model.ProductSku;
import com.patrick.redis.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

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

}
