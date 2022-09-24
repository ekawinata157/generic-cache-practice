package model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
public class Cacheable<T> {
    private T cachedItem;
    private ZonedDateTime lastAccess;
    private ZonedDateTime storedAt;
    private long accessedFrequency;

    public Cacheable(T cachedItem) {
        this.cachedItem = cachedItem;
        this.lastAccess = ZonedDateTime.now();
        this.storedAt = ZonedDateTime.now();
        this.accessedFrequency = 0;
    }

    public void updateLastAccess() {
        this.lastAccess = ZonedDateTime.now();
    }

    public void updateAccessedFrequency() {
        this.accessedFrequency++;
    }
}
