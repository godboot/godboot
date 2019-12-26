package com.godboot.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configurable
public class ApplicationConfig {
    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        int corePoolSize = Runtime.getRuntime().availableProcessors();

        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize * 2 + 1);
        threadPoolTaskExecutor.setQueueCapacity(50);
        threadPoolTaskExecutor.setKeepAliveSeconds(30);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return threadPoolTaskExecutor;
    }

    /**
     * 解决Redis插入中文乱码
     */
    @Bean
    public RedisTemplate redisTemplateInit() {
        /**
         * 设置序列化Key的实例化对象
         */
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        /**
         * 设置序列化Value的实例化对象
         */
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }
}
