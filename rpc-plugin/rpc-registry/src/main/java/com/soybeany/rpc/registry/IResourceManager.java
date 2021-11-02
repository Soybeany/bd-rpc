package com.soybeany.rpc.registry;

import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IResourceManager {

    Set<ProviderResource> load(String id);

    void save(ProviderResource resource);

    @Log
    class MapImpl implements IResourceManager {

        private final Map<String, Set<ProviderResource>> providersMap = new HashMap<>();
        private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        @Override
        public synchronized Set<ProviderResource> load(String id) {
            return providersMap.get(id);
        }

        @Override
        public synchronized void save(ProviderResource resource) {
            providersMap.computeIfAbsent(resource.getId(), k -> new HashSet<>()).add(resource);
        }

        public void startAutoClean(long validPeriodInSec) {
            long validPeriodInMillis = validPeriodInSec * 1000;
            service.scheduleWithFixedDelay(() -> {
                long time = System.currentTimeMillis();
                synchronized (this) {
                    log.info("清理");
                    providersMap.forEach((k, v) -> v.removeIf(resource -> isInvalid(time, resource, validPeriodInMillis)));
                }
            }, validPeriodInMillis, validPeriodInMillis, TimeUnit.MILLISECONDS);
        }

        public void shutdownAutoClean() {
            service.shutdown();
        }

        private boolean isInvalid(long curTimestamp, ProviderResource resource, long validPeriodInMillis) {
            long delta = curTimestamp - resource.getSyncTime().getTime();
            log.info("delta:" + delta);
            return delta > validPeriodInMillis;
        }

    }

}
