package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.registry.BaseRpcRegistrySyncerImpl;
import com.soybeany.rpc.registry.IStorageManager;
import com.soybeany.rpc.registry.StorageManagerMemImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component("RpcServiceSyncer")
public class RpcRegistrySyncerImpl extends BaseRpcRegistrySyncerImpl {

    private StorageManagerMemImpl storageManager;

    @Override
    protected IStorageManager onSetupStorageManager() {
        return storageManager = new StorageManagerMemImpl();
    }

    @PostConstruct
    private void onInit() {
        start();
        storageManager.startAutoClean(7);
    }

    @PreDestroy
    private void onDestroy() {
        storageManager.shutdownAutoClean();
        stop();
    }

}
