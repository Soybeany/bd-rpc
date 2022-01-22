package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.server.IAutoCleaner;
import com.soybeany.sync.server.StdMemStorage;

import java.util.Set;

/**
 * 需手工调用{@link #startAutoClean}与{@link #shutdownAutoClean}
 *
 * @author Soybeany
 * @date 2021/11/9
 */
public class RpcStorageManagerMemImpl implements IRpcStorageManager, IAutoCleaner {

    private final StdMemStorage<ServerInfo> storage = new StdMemStorage<>();

    @Override
    public synchronized Set<ServerInfo> load(String system, String serviceId) {
        return storage.load(getId(system, serviceId));
    }

    @Override
    public synchronized void save(String system, ServerInfo info, Set<String> serviceIds) {
        for (String serviceId : serviceIds) {
            storage.save(getId(system, serviceId), info);
        }
    }

    @Override
    public void startAutoClean(long validPeriodInSec) {
        storage.startAutoClean(validPeriodInSec);
    }

    @Override
    public void shutdownAutoClean() {
        storage.shutdownAutoClean();
    }

    // ***********************内部方法****************************

    private String getId(String system, String serviceId) {
        return system + "-" + serviceId;
    }

}
