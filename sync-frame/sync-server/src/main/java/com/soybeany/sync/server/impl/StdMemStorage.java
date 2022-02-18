package com.soybeany.sync.server.impl;

import com.soybeany.sync.server.api.IAutoCleaner;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public class StdMemStorage<T> implements IAutoCleaner {

    private final Set<T> emptySet = Collections.unmodifiableSet(new HashSet<>());
    private final Map<String, Map<T, Long>> storageMap = new HashMap<>();
    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public synchronized Set<T> load(String key) {
        return Optional.ofNullable(storageMap.get(key))
                .map(Map::keySet)
                .orElse(emptySet);
    }

    public synchronized void save(String key, T value) {
        storageMap.computeIfAbsent(key, k -> new HashMap<>()).put(value, getCurTimeInSec());
    }

    @Override
    public void startAutoClean(long validPeriodInSec) {
        service.scheduleWithFixedDelay(() -> {
            long time = getCurTimeInSec();
            synchronized (this) {
                storageMap.forEach((k, v) -> v.values().removeIf(syncTime -> isInvalid(time, syncTime, validPeriodInSec)));
            }
        }, validPeriodInSec, validPeriodInSec, TimeUnit.SECONDS);
    }

    @Override
    public void shutdownAutoClean() {
        service.shutdown();
    }

    // ***********************内部方法****************************

    private long getCurTimeInSec() {
        return System.currentTimeMillis() / 1000;
    }

    private boolean isInvalid(long curTime, long syncTime, long validPeriodInSec) {
        return curTime - syncTime > validPeriodInSec;
    }

}
