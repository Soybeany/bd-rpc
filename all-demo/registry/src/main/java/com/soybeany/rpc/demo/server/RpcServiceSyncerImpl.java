package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.registry.BaseRpcServiceSyncerImpl;
import com.soybeany.rpc.registry.IServiceManager;
import com.soybeany.rpc.registry.ServiceManagerMemImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component("RpcServiceSyncer")
public class RpcServiceSyncerImpl extends BaseRpcServiceSyncerImpl {

    private ServiceManagerMemImpl serviceManager;

    @Override
    protected IServiceManager onGetNewServiceManager(String system) {
        return serviceManager = new ServiceManagerMemImpl();
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
