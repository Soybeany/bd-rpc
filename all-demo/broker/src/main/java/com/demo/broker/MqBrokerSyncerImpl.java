package com.demo.broker;

import com.soybeany.mq.broker.BaseMqBrokerSyncerImpl;
import com.soybeany.mq.broker.api.IStorageManager;
import com.soybeany.mq.broker.impl.StorageManagerMemImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Component
public class MqBrokerSyncerImpl extends BaseMqBrokerSyncerImpl {

    private StorageManagerMemImpl manager;

    @Override
    protected String onSetupSyncUrl(String ip) {
        return getUrl(false, ip, 8083, "", "/bd-api/bSync", "");
    }

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
