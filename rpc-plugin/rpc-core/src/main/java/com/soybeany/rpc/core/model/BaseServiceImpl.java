package com.soybeany.rpc.core.model;

import com.soybeany.sync.core.api.ISyncClientConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceImpl implements ISyncClientConfig {

    @PostConstruct
    private void start() {
        onStart();
    }

    @PreDestroy
    private void stop() {
        onStop();
    }

    // ***********************子类实现****************************

    protected abstract void onStart();

    protected abstract void onStop();

    protected abstract String onSetupSystem();

    protected abstract String[] onSetupPkgPathToScan();

}
