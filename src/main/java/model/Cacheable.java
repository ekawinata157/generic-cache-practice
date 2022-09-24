package model;

import lombok.Getter;


import java.time.ZonedDateTime;

@Getter
public class Cacheable<T> {
    private final T cachedItem;
    private final ZonedDateTime storedAt;
    private ZonedDateTime lastAccess;
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
