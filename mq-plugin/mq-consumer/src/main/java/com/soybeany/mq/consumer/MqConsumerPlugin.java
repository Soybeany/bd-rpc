package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqMsgHandler;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqConsumerInput;
import com.soybeany.mq.core.model.MqConsumerOutput;
import com.soybeany.sync.core.api.IClientPlugin;

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
    private final IMqExceptionHandler exceptionHandler;
    private final Map<String, Long> topics = new HashMap<>();

    public MqConsumerPlugin(List<IMqMsgHandler> msgHandlers, IMqExceptionHandler exceptionHandler) {
        msgHandlers.forEach(handler ->
                msgHandlerMap.computeIfAbsent(handler.onSetupTopic(), topic -> new ArrayList<>()).add(handler)
        );
        this.exceptionHandler = (null != exceptionHandler ? exceptionHandler : new IMqExceptionHandler.LogImpl());
        this.msgHandlerMap.keySet().forEach(topic -> topics.put(topic, 0L));
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
    public synchronized void onHandleOutput(String uid, MqConsumerOutput output) throws Exception {
        IClientPlugin.super.onHandleOutput(uid, output);
        output.getTopics().putAll(topics);
    }

    @Override
    public synchronized void onHandleInput(String uid, MqConsumerInput input) throws Exception {
        IClientPlugin.super.onHandleInput(uid, input);
        input.getMessages().forEach((topic, message) -> {
            // 更新topic的戳
            topics.put(topic, message.getStamp());
            // 事件分发
            msgHandlerMap.get(topic).forEach(handler -> message.getMessages().forEach(msg -> {
                try {
                    handler.onHandle(msg);
                } catch (Exception e) {
                    exceptionHandler.onException(e, msg, handler);
                }
            }));
        });
    }
}
