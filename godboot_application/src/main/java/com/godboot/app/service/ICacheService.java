package com.godboot.app.service;

import com.godboot.app.cache.Cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICacheService {
    Boolean cacheValue(Cache cache, String key, Object value);

    <T> T getCacheValue(Cache cache, String key, Class<T> requiredType);

    Boolean cacheList(Cache cache, String key, List<?> value);

    <T> List<T>  getCacheList(Cache cache, String key, Class<T> requiredType);

    Boolean cacheSet(Cache cache, String key, Set<?> value);

    <T> Set<T>  getCacheSet(Cache cache, String key, Class<T> requiredType);

    Boolean cacheHash(Cache cache, String key, Map<Object, Object> value);

    <T> Map<Object, T>  getCacheHash(Cache cache, String key, Class<T> requiredType);
}
