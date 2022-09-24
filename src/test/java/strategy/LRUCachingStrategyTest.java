package strategy;

import interfaces.CachingStrategy;
import model.Cacheable;
import model.ConcreteModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static model.ConcreteModel.getConcreteModel;

public class LRUCachingStrategyTest {
    @Test
    public void evict_shouldRemoveLeastRecentlyUsedEntry_whenMapEntryIsNotNull(){
        Map<Long, Cacheable<ConcreteModel>> storageMap = new HashMap<>();
        Cacheable<ConcreteModel> firstItem = new Cacheable<>(getConcreteModel(1, "Max"));
        Cacheable<ConcreteModel> secondItem = new Cacheable<>(getConcreteModel(2, "Andy"));
        storageMap.put(firstItem.getCachedItem().getId(), firstItem);
        storageMap.put(secondItem.getCachedItem().getId(), secondItem);
        CachingStrategy<ConcreteModel, Long> lruCachingStrategy = new LRUCachingStrategy<>();

        firstItem.updateLastAccess();
        lruCachingStrategy.evict(storageMap);

        Assert.assertNull(storageMap.get(secondItem.getCachedItem().getId()));
        Assert.assertEquals(storageMap.get(firstItem.getCachedItem().getId()), firstItem);
    }
}
