package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.registry.BaseServiceSyncerImpl;
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
public class ServiceSyncerImpl extends BaseServiceSyncerImpl {

    private ServiceManagerAutoCleanImpl serviceManager;

    @Override
    protected String[] onSetupAcceptableSystems() {
        return new String[]{null};
    }

    @Override
    protected IServiceManager onGetNewServiceManager(String system) {
        return serviceManager = new ServiceManagerAutoCleanImpl();
    }

    @PostConstruct
    private void onInit() {
        start();
        serviceManager.startAutoClean(7);
    }

    @PreDestroy
    private void onDestroy() {
        serviceManager.shutdownAutoClean();
        stop();
    }

}
