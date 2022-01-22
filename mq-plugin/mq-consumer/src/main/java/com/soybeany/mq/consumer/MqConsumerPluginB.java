package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqMsgHandler;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqConsumerInputB;
import com.soybeany.mq.core.model.MqConsumerOutputB;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public class MqConsumerPluginB implements IClientPlugin<MqConsumerInputB, MqConsumerOutputB> {

    private final Map<String, List<IMqMsgHandler>> msgHandlerMap = new HashMap<>();
    private final IMqExceptionHandler exceptionHandler;
    private final Map<String, Long> topics = new HashMap<>();

    public MqConsumerPluginB(List<IMqMsgHandler> msgHandlers, IMqExceptionHandler exceptionHandler) {
        msgHandlers.forEach(handler ->
                msgHandlerMap.computeIfAbsent(handler.onSetupTopic(), topic -> new ArrayList<>()).add(handler)
        );
        this.exceptionHandler = (null != exceptionHandler ? exceptionHandler : new IMqExceptionHandler.LogImpl());
        this.msgHandlerMap.keySet().forEach(topic -> topics.put(topic, 0L));
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C_B;
    }

    @Override
    public Class<MqConsumerInputB> onGetInputClass() {
        return MqConsumerInputB.class;
    }

    @Override
    public Class<MqConsumerOutputB> onGetOutputClass() {
        return MqConsumerOutputB.class;
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, MqConsumerOutputB output) throws Exception {
        output.getTopics().putAll(topics);
        return IClientPlugin.super.onBeforeSync(uid, output);
    }

    @Override
    public synchronized void onAfterSync(String uid, MqConsumerInputB input) throws Exception {
        IClientPlugin.super.onAfterSync(uid, input);
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
