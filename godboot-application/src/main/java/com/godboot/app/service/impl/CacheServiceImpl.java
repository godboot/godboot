package com.godboot.app.service.impl;

import com.godboot.app.cache.Cache;
import com.godboot.app.service.ICacheService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements ICacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean cacheValue(Cache cache, String key, Object value) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        redisTemplate.opsForValue().set(cache.prefix() + "_" + key, value, cache.expireTime(), TimeUnit.SECONDS);

        return Boolean.TRUE;
    }

    @Override
    public <T> T getCacheValue(Cache cache, String key, Class<T> requiredType) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        Object o = redisTemplate.opsForValue().get(cache.prefix() + "_" + key);

        return (T) o;
    }

    @Override
    public Boolean cacheList(Cache cache, String key, List<?> value) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        Long count = redisTemplate.opsForList().leftPush(cache.prefix() + "_" + key, value);

        return count > 0;
    }

    @Override
    public <T> List<T> getCacheList(Cache cache, String key, Class<T> requiredType) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        Object o = redisTemplate.opsForList().leftPop(cache.prefix() + "_" + key);

        return (List<T>) o;
    }

    @Override
    public Boolean cacheSet(Cache cache, String key, Set<?> value) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        Long count = redisTemplate.opsForSet().add(cache.prefix() + "_" + key, value);

        return count > 0;
    }

    @Override
    public <T> Set<T> getCacheSet(Cache cache, String key, Class<T> requiredType) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        Object o = redisTemplate.opsForSet().pop(cache.prefix() + "_" + key);

        return (Set<T>) o;
    }

    @Override
    public Boolean cacheHash(Cache cache, String key, Map<Object, Object> value) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        redisTemplate.opsForHash().putAll(cache.prefix() + "_" + key, value);

        return Boolean.TRUE;
    }

    @Override
    public <T> Map<Object, T> getCacheHash(Cache cache, String key, Class<T> requiredType) {
        if (cache == null || StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存元数据不能为空");
        }

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cache.prefix() + "_" + key);

        return (Map<Object, T>) entries;
    }
}
