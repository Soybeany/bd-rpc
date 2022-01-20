package com.soybeany.rpc.demo.server;

import com.soybeany.mq.broker.BaseMqServiceSyncerImpl;
import com.soybeany.mq.broker.IMessageManager;
import com.soybeany.mq.broker.MessageManagerMemImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Component("MqServiceSyncer")
public class MqServiceSyncerImpl extends BaseMqServiceSyncerImpl {

    private MessageManagerMemImpl manager;

    @Override
    protected IMessageManager onSetupMessageManager() {
        return manager = new MessageManagerMemImpl();
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
