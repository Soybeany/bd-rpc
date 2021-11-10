package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.registry.BaseRpcRegistryPlugin;
import com.soybeany.rpc.registry.IServiceManager;
import com.soybeany.rpc.registry.ServiceManagerAutoCleanImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class RegistryPluginImpl extends BaseRpcRegistryPlugin {

    private ServiceManagerAutoCleanImpl serviceManager;

    @Override
    protected IServiceManager onSetupServiceManager() {
        return serviceManager = new ServiceManagerAutoCleanImpl();
    }

    @PostConstruct
    private void onInit() {
        serviceManager.startAutoClean(7);
    }

    @PreDestroy
    private void onDestroy() {
        serviceManager.shutdownAutoClean();
    }

}
