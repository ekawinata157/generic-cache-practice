package strategy;

import interfaces.CachingStrategy;
import model.Cacheable;

import java.util.Comparator;
import java.util.Map;

public class LRUCachingStrategy<T, ID> implements CachingStrategy<T, ID> {
    @Override
    public void evict(Map<ID, Cacheable<T>> storageMap) {
        storageMap.entrySet().stream()
                .min(Comparator.comparing(a -> a.getValue().getLastAccess()))
                .ifPresent(es -> storageMap.remove(es.getKey()));
    }
}
