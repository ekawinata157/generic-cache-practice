package strategy;

import interfaces.CachingStrategy;
import model.Cacheable;

import java.util.Comparator;
import java.util.Map;

public class LFUCachingStrategy<T, ID> implements CachingStrategy<T, ID> {
    @Override
    public void evict(Map<ID, Cacheable<T>> storageMap) {
        Comparator<Cacheable<T>> frequencyAccessComparator = (a, b) -> {
            if (a.getAccessedFrequency() == b.getAccessedFrequency()) {
                return a.getStoredAt().compareTo(b.getStoredAt());
            }
            return Long.compare(a.getAccessedFrequency(), b.getAccessedFrequency());
        };

        storageMap.entrySet().stream()
                .min((a, b) -> frequencyAccessComparator.compare(a.getValue(), b.getValue()))
                .ifPresent(cachedItem -> storageMap.remove(cachedItem.getKey()));
    }
}
