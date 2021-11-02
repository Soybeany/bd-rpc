package com.soybeany.rpc.registry;

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

    class MapImpl implements IResourceManager {

        private final Map<String, Set<ProviderResource>> providersMap = new HashMap<>();
        private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        @Override
        public synchronized Set<ProviderResource> load(String id) {
            return providersMap.get(id);
        }

        @Override
        public synchronized void save(ProviderResource resource) {
            Set<ProviderResource> set = providersMap.computeIfAbsent(resource.getId(), k -> new HashSet<>());
            set.remove(resource);
            set.add(resource);
        }

        public void startAutoClean(long validPeriodInSec) {
            long validPeriodInMillis = validPeriodInSec * 1000;
            service.scheduleWithFixedDelay(() -> {
                long time = System.currentTimeMillis();
                synchronized (this) {
                    providersMap.forEach((k, v) -> v.removeIf(resource -> isInvalid(time, resource, validPeriodInMillis)));
                }
            }, validPeriodInMillis, validPeriodInMillis, TimeUnit.MILLISECONDS);
        }

        public void shutdownAutoClean() {
            service.shutdown();
        }

        private boolean isInvalid(long curTimestamp, ProviderResource resource, long validPeriodInMillis) {
            long delta = curTimestamp - resource.getSyncTime().getTime();
            return delta > validPeriodInMillis;
        }

    }

}
