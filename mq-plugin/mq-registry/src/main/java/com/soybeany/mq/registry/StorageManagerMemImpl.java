package com.soybeany.mq.registry;

import com.soybeany.sync.server.IAutoCleaner;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public class StorageManagerMemImpl implements IStorageManager, IAutoCleaner {

    private static final Set<String> EMPTY_SET = Collections.unmodifiableSet(new HashSet<>());
    private final Map<String, Map<String, Long>> providersMap = new HashMap<>();
    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    @Override
    public synchronized Set<String> load(String system) {
        return Optional.ofNullable(providersMap.get(system))
                .map(Map::keySet)
                .orElse(EMPTY_SET);
    }

    @Override
    public synchronized void save(String system, String syncUrl) {
        providersMap.computeIfAbsent(system, s -> new HashMap<>()).put(syncUrl, System.currentTimeMillis());
    }

    @Override
    public void startAutoClean(long validPeriodInSec) {
        long validPeriodInMillis = validPeriodInSec * 1000;
        service.scheduleWithFixedDelay(() -> {
            long time = System.currentTimeMillis();
            synchronized (this) {
                providersMap.forEach((k, v) -> v.values().removeIf(syncTime -> isInvalid(time, syncTime, validPeriodInMillis)));
            }
        }, validPeriodInMillis, validPeriodInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void shutdownAutoClean() {
        service.shutdown();
    }

    // ***********************内部方法****************************

    private boolean isInvalid(long curTimestamp, long syncTime, long validPeriodInMillis) {
        return curTimestamp - syncTime > validPeriodInMillis;
    }

}
