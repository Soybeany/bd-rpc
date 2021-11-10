package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.registry.BaseRpcRegistryPlugin;
import com.soybeany.rpc.registry.IResourceManager;
import com.soybeany.rpc.registry.ResourceManagerAutoCleanImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class RegistryPluginImpl extends BaseRpcRegistryPlugin {

    private ResourceManagerAutoCleanImpl resourceManager;

    @Override
    protected IResourceManager onSetupResourceManager() {
        return resourceManager = new ResourceManagerAutoCleanImpl(2);
    }

    @PostConstruct
    private void onInit() {
        resourceManager.startAutoClean(7);
    }

    @PreDestroy
    private void onDestroy() {
        resourceManager.shutdownAutoClean();
    }

}
