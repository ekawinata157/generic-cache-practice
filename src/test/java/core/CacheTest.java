package core;

import exceptions.CacheKeyTypeMismatchException;
import exceptions.UncacheableException;
import interfaces.CachingStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import model.Cacheable;
import model.ConcreteModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static model.ConcreteModel.getConcreteModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class CacheTest {
    @Test
    public void put_shouldStoreDataInCache_whenSpaceIsAvailable() throws IllegalAccessException {
        Cache<ConcreteModel, Long> cache = new Cache<>(new ConcreteCachingStrategy<>(), 1, long.class);
        ConcreteModel concreteModel = getConcreteModel(1, "Test Model");

        cache.put(concreteModel);

        Assert.assertEquals(concreteModel, cache.get(concreteModel.getId()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void put_shouldEvictDataFirst_whenSpaceIsNotAvailable() throws IllegalAccessException {
        ConcreteCachingStrategy<ConcreteModel, Long> strategy = (ConcreteCachingStrategy<ConcreteModel, Long>) Mockito.mock(ConcreteCachingStrategy.class);
        Cache<ConcreteModel, Long> cache = new Cache<>(strategy, 1, long.class);
        ConcreteModel firstConcreteModel = getConcreteModel(1, "Test Model");
        ConcreteModel secondConcreteModel = getConcreteModel(2, "Test Model 2");

        cache.put(firstConcreteModel);
        cache.put(secondConcreteModel);

        Mockito.verify(strategy, times(1)).evict(any());
    }

    @Test
    public void get_shouldReturnNull_whenItemWasNotInCache() {
        Cache<ConcreteModel, Long> cache = new Cache<>(new ConcreteCachingStrategy<>(), 1, long.class);

        ConcreteModel result = cache.get(1L);

        Assert.assertNull(result);
    }

    @Test
    public void get_shouldReturnItem_whenItemWasInCache() {
        Cache<ConcreteModel, Long> cache = new Cache<>(new ConcreteCachingStrategy<>(), 1, long.class);

        ConcreteModel result = cache.get(1L);

        Assert.assertNull(result);
    }

    @Test(expected = CacheKeyTypeMismatchException.class)
    public void put_shouldThrowException_whenKeyTypeIsNotMatching() throws IllegalAccessException {
        Cache<ConcreteModel, String> cache = new Cache<>(new ConcreteCachingStrategy<>(), 1, String.class);
        ConcreteModel concreteModel = new ConcreteModel(1, "Test Concrete Model");

        cache.put(concreteModel);
    }

    @Test(expected = UncacheableException.class)
    public void put_shouldThrowException_whenModelIsUncacheable() throws IllegalAccessException {
        Cache<UncacheableModel, String> cache = new Cache<>(new ConcreteCachingStrategy<>(), 1, String.class);
        UncacheableModel uncacheableModel = new UncacheableModel(1, "Uncacheable model");

        cache.put(uncacheableModel);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void put_shouldAdjustEvictionOnTheFly_whenCachingStrategyIsChanged() throws IllegalAccessException {
        ConcreteCachingStrategy<ConcreteModel, Long> primaryCachingStrategy = (ConcreteCachingStrategy<ConcreteModel, Long>) Mockito.mock(ConcreteCachingStrategy.class);
        CachingStrategy<ConcreteModel, Long> secondaryCachingStrategy = (CachingStrategy<ConcreteModel, Long>) Mockito.mock(CachingStrategy.class);

        Cache<ConcreteModel, Long> cache = new Cache<>(primaryCachingStrategy, 1, long.class);
        ConcreteModel firstConcreteModel = new ConcreteModel(1L, "First Concrete Model");
        ConcreteModel secondConcreteModel = new ConcreteModel(2L, "Second Concrete Model");
        ConcreteModel thirdConcreteModel = new ConcreteModel(3L, "Third Concrete Model");

        cache.put(firstConcreteModel);
        cache.put(secondConcreteModel);
        cache.setCachingStrategy(secondaryCachingStrategy);
        cache.put(thirdConcreteModel);

        Mockito.verify(primaryCachingStrategy, times(1)).evict(any());
        Mockito.verify(secondaryCachingStrategy, times(1)).evict(any());
    }

    @Test
    public void getIdToCacheableMap_shouldReturnStorageMap_whenInvoked() {
        Cache<ConcreteModel, Long> cache = new Cache<>(new ConcreteCachingStrategy<>(), 1, long.class);

        Map<Long, Cacheable<ConcreteModel>> storageMap = cache.getIdToCacheableMap();

        Assert.assertNotNull(storageMap);
    }

    private static class ConcreteCachingStrategy<T, ID> implements CachingStrategy<T, ID> {
        @Override
        public void evict(Map<ID, Cacheable<T>> storageMap) {

        }
    }

    @Data
    @AllArgsConstructor
    private static class UncacheableModel {
        private long id;
        private String name;
    }
}
