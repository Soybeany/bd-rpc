package com.soybeany.rpc.demo.server;

import com.soybeany.mq.broker.BaseMqBrokerSyncerImpl;
import com.soybeany.mq.broker.IStorageManager;
import com.soybeany.mq.broker.StorageManagerMemImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Component("MqServiceSyncer")
public class MqBrokerSyncerImpl extends BaseMqBrokerSyncerImpl {

    private StorageManagerMemImpl manager;

    @Override
    protected IStorageManager onSetupStorageManager() {
        return manager = new StorageManagerMemImpl();
    }

    @PostConstruct
    private void onInit() {
        start();
        manager.startAutoClean(7);
    }

    @PreDestroy
    private void onDestroy() {
        manager.shutdownAutoClean();
        stop();
    }

}
