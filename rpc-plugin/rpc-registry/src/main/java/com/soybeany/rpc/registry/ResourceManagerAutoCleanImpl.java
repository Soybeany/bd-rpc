package com.soybeany.rpc.registry;

import com.soybeany.sync.core.util.RequestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.soybeany.rpc.core.model.BdRpcConstants.PATH_CHECK;
import static com.soybeany.rpc.core.model.BdRpcConstants.RESULT_OK;

/**
 * @author Soybeany
 * @date 2021/11/9
 */
public class ResourceManagerAutoCleanImpl implements IResourceManager {

    private final Map<String, Set<ProviderResource>> providersMap = new HashMap<>();
    private final ScheduledExecutorService service;

    public ResourceManagerAutoCleanImpl(int cleanPoolSize) {
        //noinspection AlibabaThreadPoolCreation
        service = Executors.newScheduledThreadPool(cleanPoolSize);
    }

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

    // ***********************内部方法****************************

    private boolean isInvalid(long curTimestamp, ProviderResource resource, long validPeriodInMillis) {
        boolean isInvalid = curTimestamp - resource.getSyncTime() > validPeriodInMillis;
        if (!isInvalid) {
            return false;
        }
        try {
            String result = RequestUtils.request(resource.getInfo().toUrl(PATH_CHECK), null, null, String.class);
            if (RESULT_OK.equals(result)) {
                resource.setSyncTime(curTimestamp);
                return false;
            }
            return true;
        } catch (IOException e) {
            return true;
        }
    }

}
