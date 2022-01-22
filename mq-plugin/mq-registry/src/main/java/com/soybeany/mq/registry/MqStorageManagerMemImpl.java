package com.soybeany.mq.registry;

import com.soybeany.sync.server.IAutoCleaner;
import com.soybeany.sync.server.StdMemStorage;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public class MqStorageManagerMemImpl implements IMqStorageManager, IAutoCleaner {

    StdMemStorage<String> storage = new StdMemStorage<>();

    @Override
    public synchronized Set<String> load(String system) {
        return storage.load(system);
    }

    @Override
    public synchronized void save(String system, String syncUrl) {
        storage.save(system, syncUrl);
    }

    @Override
    public void startAutoClean(long validPeriodInSec) {
        storage.startAutoClean(validPeriodInSec);
    }

    @Override
    public void shutdownAutoClean() {
        storage.shutdownAutoClean();
    }

}
