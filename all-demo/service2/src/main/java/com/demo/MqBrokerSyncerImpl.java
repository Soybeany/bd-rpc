package com.demo;

import com.soybeany.mq.consumer.BaseMqBrokerSyncerImpl;
import com.soybeany.mq.consumer.IMqExceptionHandler;
import com.soybeany.mq.consumer.ITopicInfoRepository;
import com.soybeany.mq.consumer.TopicInfoRepositoryMemImpl;
import com.soybeany.mq.core.api.IMqBrokerSyncUrlProvider;
import com.soybeany.mq.core.api.IMqMsgHandler;
import com.soybeany.rpc.core.api.IRpcServiceProxy;
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
public class MqBrokerSyncerImpl extends BaseMqBrokerSyncerImpl {

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
