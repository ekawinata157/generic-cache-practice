package interfaces;

import model.Cacheable;

import java.util.Map;

public interface CachingStrategy<T, ID> {
    void evict(Map<ID, Cacheable<T>> storageMap);
}
