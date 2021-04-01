package com.patrick.redis.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    @SuppressWarnings("rawtypes")
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new AlibabaJsonRedisSerializer<Object>(Object.class);
    }

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory, RedisSerializer fastJson2JsonRedisSerializer) {
        RedisTemplate template = new StringRedisTemplate(factory);

        template.setValueSerializer(fastJson2JsonRedisSerializer);
        //设置hash 序列化
        template.setHashKeySerializer(fastJson2JsonRedisSerializer);
        template.setHashValueSerializer(fastJson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置Redisson
     *
     * @return Redisson
     */
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}