package com.demo;

import com.soybeany.rpc.registry.RpcRegistryPlugin;
import com.soybeany.rpc.registry.RpcStorageManagerMemImpl;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.server.BaseServerSyncerImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class RegistrySyncerImpl extends BaseServerSyncerImpl {

    private RpcStorageManagerMemImpl rpcStorageManager;

    @Override
    protected void onSetupPlugins(List<IServerPlugin<?, ?>> plugins) {
        plugins.addAll(RpcRegistryPlugin.get(rpcStorageManager = new RpcStorageManagerMemImpl()));
    }

    @PostConstruct
    private void onInit() {
        start();
        rpcStorageManager.startAutoClean(7);
    }

    @PreDestroy
    private void onDestroy() {
        rpcStorageManager.shutdownAutoClean();
        stop();
    }

}
