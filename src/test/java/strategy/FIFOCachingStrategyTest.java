package strategy;

import interfaces.CachingStrategy;
import model.Cacheable;
import model.ConcreteModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static model.ConcreteModel.getConcreteModel;

public class FIFOCachingStrategyTest {
    @Test
    public void evict_shouldRemoveOldestEntry_whenMapEntryIsNotNull() {
        Map<Long, Cacheable<ConcreteModel>> storageMap = new HashMap<>();
        Cacheable<ConcreteModel> firstItem = new Cacheable<>(getConcreteModel(1, "Max"));
        Cacheable<ConcreteModel> secondItem = new Cacheable<>(getConcreteModel(2, "Andy"));
        storageMap.put(firstItem.getCachedItem().getId(), firstItem);
        storageMap.put(secondItem.getCachedItem().getId(), secondItem);
        CachingStrategy<ConcreteModel, Long> fifoCachingStrategy = new FIFOCachingStrategy<>();

        fifoCachingStrategy.evict(storageMap);

        Assert.assertNull(storageMap.get(firstItem.getCachedItem().getId()));
        Assert.assertEquals(storageMap.get(secondItem.getCachedItem().getId()), secondItem);
    }
}
