package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.server.IAutoCleaner;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 需手工调用{@link #startAutoClean}与{@link #shutdownAutoClean}
 *
 * @author Soybeany
 * @date 2021/11/9
 */
public class ServiceManagerMemImpl implements IServiceManager, IAutoCleaner {

    private static final Set<ServerInfo> EMPTY_SET = Collections.unmodifiableSet(new HashSet<>());
    private final Map<String, Map<ServerInfo, Long>> providersMap = new HashMap<>();
    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    @Override
    public synchronized Set<ServerInfo> load(String id) {
        return Optional.ofNullable(providersMap.get(id))
                .map(Map::keySet)
                .orElse(EMPTY_SET);
    }

    @Override
    public synchronized void save(ServerInfo info, Set<String> serviceIds) {
        long time = System.currentTimeMillis();
        for (String serviceId : serviceIds) {
            providersMap.computeIfAbsent(serviceId, id -> new HashMap<>()).put(info, time);
        }
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
