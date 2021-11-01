package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.registry.BaseRpcRegistryPlugin;
import com.soybeany.rpc.registry.IResourceManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class TestPlugin extends BaseRpcRegistryPlugin {

    @Override
    protected IResourceManager onSetupResourceManager() {
        return new IResourceManager.MapImpl(3);
    }

    @PostConstruct
    private void onInit() {
        ((IResourceManager.MapImpl) resourceManager).startAutoClean(7);
    }

    @PreDestroy
    private void onDestroy() {
        ((IResourceManager.MapImpl) resourceManager).shutdownAutoClean();
    }

}
