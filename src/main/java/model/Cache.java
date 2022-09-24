package model;

import annotation.CacheID;
import interfaces.CachingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cache<T, ID> {
    private final Class<ID> idType;
    @Getter
    private final Map<ID, Cacheable<T>> idToCacheableMap;
    @Setter
    private CachingStrategy<T, ID> cachingStrategy;
    private long size;
    private final long capacity;

    public Cache(CachingStrategy<T, ID> cachingStrategy, long capacity, Class<ID> idType) {
        this.idToCacheableMap = new HashMap<>();
        this.cachingStrategy = cachingStrategy;
        this.capacity = capacity;
        this.size = 0;
        this.idType = idType;
    }

    public void put(T t) throws IllegalAccessException {
        if (size >= capacity) {
            cachingStrategy.evict(this.idToCacheableMap);
            size--;
        }
        idToCacheableMap.put(this.getId(t), new Cacheable<>(t));
        size++;
    }

    public T get(ID id) {
        if (idToCacheableMap.containsKey(id)) {
            Cacheable<T> cachedItem = idToCacheableMap.get(id);
            updateCachedItem(cachedItem);
            return idToCacheableMap.get(id).getCachedItem();
        }
        return null;
    }

    private void updateCachedItem(Cacheable<T> cachedItem) {
        cachedItem.updateAccessedFrequency();
        cachedItem.updateLastAccess();
    }

    private ID getId(T t) throws IllegalAccessException {
        Field idField = Arrays.stream(t.getClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(CacheID.class))
                .findFirst()
                .orElse(null);
        if (idField == null) {
            throw new RuntimeException("Cannot be cached!");
        }
        if (!idField.getType().equals(this.idType)) {
            throw new RuntimeException("Unsuitable ID type!");
        }
        idField.setAccessible(true);
        return (ID) idField.get(t);
    }
}
