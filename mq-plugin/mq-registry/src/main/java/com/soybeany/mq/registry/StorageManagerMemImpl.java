package com.soybeany.mq.registry;

import com.soybeany.sync.server.IAutoCleaner;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public class StorageManagerMemImpl implements IStorageManager, IAutoCleaner {

//    private IDataHolder<>

    @Override
    public Set<String> load(String system) {
        return null;
    }

    @Override
    public void save(String system, String syncUrl) {

    }

    @Override
    public void startAutoClean(long validPeriodInSec) {

    }

    @Override
    public void shutdownAutoClean() {

    }

}
