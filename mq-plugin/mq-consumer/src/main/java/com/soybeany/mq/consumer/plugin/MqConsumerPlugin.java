package com.soybeany.mq.consumer.plugin;

import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.IMqMsgHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import com.soybeany.mq.consumer.impl.MqExceptionHandlerLogImpl;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqConsumerInput;
import com.soybeany.mq.core.model.MqConsumerOutput;
import com.soybeany.mq.core.model.MqTopicInfo;
import com.soybeany.sync.client.api.IClientPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public class MqConsumerPlugin implements IClientPlugin<MqConsumerInput, MqConsumerOutput> {

    private final Map<String, List<IMqMsgHandler>> msgHandlerMap = new HashMap<>();
    private final ITopicInfoRepository repository;
    private final IMqExceptionHandler exceptionHandler;

    public MqConsumerPlugin(List<IMqMsgHandler> msgHandlers, ITopicInfoRepository repository, IMqExceptionHandler exceptionHandler) {
        msgHandlers.forEach(handler -> msgHandlerMap.computeIfAbsent(handler.onSetupTopic(), topic -> new ArrayList<>()).add(handler));
        this.repository = repository;
        this.exceptionHandler = (null != exceptionHandler ? exceptionHandler : new MqExceptionHandlerLogImpl());
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C;
    }

    @Override
    public Class<MqConsumerInput> onGetInputClass() {
        return MqConsumerInput.class;
    }

    @Override
    public Class<MqConsumerOutput> onGetOutputClass() {
        return MqConsumerOutput.class;
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, MqConsumerOutput output) throws Exception {
        output.getTopics().addAll(repository.getAll(msgHandlerMap.keySet()));
        return IClientPlugin.super.onBeforeSync(uid, output);
    }

    @Override
    public synchronized void onAfterSync(String uid, MqConsumerInput input) throws Exception {
        IClientPlugin.super.onAfterSync(uid, input);
        input.getMessages().forEach((topic, message) -> {
            // 事件分发
            boolean enableUpdate = true;
            for (IMqMsgHandler handler : msgHandlerMap.get(topic)) {
                try {
                    handler.onHandle(message.getMessages());
                } catch (Exception e) {
                    boolean canUpdate = exceptionHandler.onException(e, message.getMessages(), handler);
                    if (!canUpdate) {
                        enableUpdate = false;
                        break;
                    }
                }
            }
            // 更新topic的戳
            if (enableUpdate) {
                repository.updateTopicInfo(new MqTopicInfo(topic, message.getStamp()));
            }
        });
    }
}
