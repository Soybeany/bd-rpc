package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqMsgSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqProducerInput;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.core.model.MqProducerOutput;
import com.soybeany.sync.core.api.IClientPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Slf4j
public class MqProducerPlugin implements IClientPlugin<MqProducerInput, MqProducerOutput>, IMqMsgSender {

    private final Map<String, Holder> buffer = new HashMap<>();
    private final Map<String, List<IMqMsgSendCallback>> callbackMap = new HashMap<>();

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_P;
    }

    @Override
    public Class<MqProducerInput> onGetInputClass() {
        return MqProducerInput.class;
    }

    @Override
    public Class<MqProducerOutput> onGetOutputClass() {
        return MqProducerOutput.class;
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, MqProducerOutput output) throws Exception {
        // 当缓冲区没数据时，不需执行同步
        if (buffer.isEmpty()) {
            return false;
        }
        // 处理缓冲
        buffer.forEach((topic, holder) -> {
            output.getMessages().put(topic, holder.msgList);
            callbackMap.put(uid, holder.callbackList);
        });
        // 清空缓冲
        buffer.clear();
        return IClientPlugin.super.onBeforeSync(uid, output);
    }

    @Override
    public synchronized void onAfterSync(String uid, MqProducerInput input) throws Exception {
        IClientPlugin.super.onAfterSync(uid, input);
        handleInput(uid, input);
    }

    @Override
    public synchronized void onSyncException(String uid, Exception e) throws Exception {
        IClientPlugin.super.onSyncException(uid, e);
        handleInput(uid, new MqProducerInput(false, e.getMessage()));
    }

    @Override
    public synchronized void asyncSend(String topic, MqProducerMsg msg, IMqMsgSendCallback callback) {
        buffer.computeIfAbsent(topic, t -> new Holder()).add(msg, callback);
    }

    // ***********************内部方法****************************

    private synchronized void handleInput(String uid, MqProducerInput input) {
        List<IMqMsgSendCallback> callbacks = callbackMap.remove(uid);
        if (null == callbacks) {
            log.warn("无法找到uid(" + uid + ")的归档");
            return;
        }
        // 调用异步回调
        for (IMqMsgSendCallback callback : callbacks) {
            try {
                callback.onFinish(input);
            } catch (Exception ignore) {
            }
        }
    }

    // ***********************内部类****************************

    private static class Holder {
        final List<MqProducerMsg> msgList = new ArrayList<>();
        final List<IMqMsgSendCallback> callbackList = new ArrayList<>();

        public void add(MqProducerMsg msg, IMqMsgSendCallback callback) {
            msgList.add(msg);
            if (null != callback) {
                callbackList.add(callback);
            }
        }
    }

}
