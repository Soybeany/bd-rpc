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
    private final Map<String, String> actStamps = new HashMap<>();

    public MqConsumerPlugin(List<IMqMsgHandler> msgHandlers, IMqExceptionHandler exceptionHandler) {
        msgHandlers.forEach(handler ->
                msgHandlerMap.computeIfAbsent(handler.onSetupTopic(), topic -> new ArrayList<>()).add(handler)
        );
        this.exceptionHandler = (null != exceptionHandler ? exceptionHandler : new IMqExceptionHandler.LogImpl());
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
        output.getActStamps().putAll(actStamps);
        output.setTopics(msgHandlerMap.keySet());
    }

    @Override
    public synchronized void onHandleInput(String uid, MqConsumerInput input) throws Exception {
        IClientPlugin.super.onHandleInput(uid, input);
        actStamps.clear();
        // 分发事件
        input.getMessages().forEach((topic, message) -> msgHandlerMap.get(topic).forEach(handler -> message.getPayloads().forEach(payload -> {
            try {
                handler.onHandle(payload);
            } catch (Exception e) {
                exceptionHandler.onException(e, payload, handler);
            }
        })));
    }
}
