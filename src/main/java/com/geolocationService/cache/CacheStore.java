package com.geolocationService.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheStore<T> {
    private Cache<String, T> cache;


    //Constructor
    public CacheStore(int expiryDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    //fetch previous record with the stored key
    public T get(String key) {
        return cache.getIfPresent(key);
    }

    //add a new record
    public void add(String key, T value) {
        if (key != null && value != null) {
            cache.put(key, value);
            System.out.println("Record has been stored in " + value.getClass().getSimpleName() + " the Cache with Key = " + key);
        }
    }
}
