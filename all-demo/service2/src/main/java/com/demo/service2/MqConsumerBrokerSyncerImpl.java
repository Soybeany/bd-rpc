package com.demo.service2;

import com.soybeany.mq.consumer.BaseMqConsumerBrokerSyncerImpl;
import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.IMqMsgHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import com.soybeany.mq.consumer.impl.TopicInfoRepositoryMemImpl;
import com.soybeany.mq.core.api.IMqBrokerSyncUrlProvider;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Component
public class MqConsumerBrokerSyncerImpl extends BaseMqConsumerBrokerSyncerImpl {

    @Autowired
    private List<IMqMsgHandler> handlers;

    @Autowired
    private IRpcServiceProxy serviceProxy;

    @Override
    protected List<IMqMsgHandler> onSetupMsgHandlers() {
        return handlers;
    }

    @Override
    protected ITopicInfoRepository onSetupTopicInfoRepository() {
        return new TopicInfoRepositoryMemImpl();
    }

    @Override
    protected IMqExceptionHandler onSetupExceptionHandler() {
        return null;
    }

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
