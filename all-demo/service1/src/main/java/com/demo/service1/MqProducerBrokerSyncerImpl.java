package com.demo.service1;

import com.soybeany.mq.core.api.IMqBrokerSyncUrlProvider;
import com.soybeany.mq.producer.BaseMqProducerBrokerSyncerImpl;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
@Component
public class MqProducerBrokerSyncerImpl extends BaseMqProducerBrokerSyncerImpl {

    @Autowired
    private IRpcServiceProxy serviceProxy;

    @Override
    protected IMqBrokerSyncUrlProvider onGetMqBrokerSyncUrlProvider() {
        return serviceProxy.get(IMqBrokerSyncUrlProvider.class);
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }

    @PostConstruct
    private void onInit() {
        start();
    }

    @PreDestroy
    private void onDestroy() {
        stop();
    }

}
