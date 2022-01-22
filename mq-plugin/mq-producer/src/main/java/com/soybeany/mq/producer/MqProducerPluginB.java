package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqMsgSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqProducerInputB;
import com.soybeany.mq.core.model.MqProducerMsgB;
import com.soybeany.mq.core.model.MqProducerOutputB;
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
public class MqProducerPluginB implements IClientPlugin<MqProducerInputB, MqProducerOutputB>, IMqMsgSender {

    private final Map<String, Holder> buffer = new HashMap<>();
    private final Map<String, List<IMqMsgSendCallback>> callbackMap = new HashMap<>();

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_P_B;
    }

    @Override
    public Class<MqProducerInputB> onGetInputClass() {
        return MqProducerInputB.class;
    }

    @Override
    public Class<MqProducerOutputB> onGetOutputClass() {
        return MqProducerOutputB.class;
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, MqProducerOutputB output) throws Exception {
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
    public synchronized void onAfterSync(String uid, MqProducerInputB input) throws Exception {
        IClientPlugin.super.onAfterSync(uid, input);
        handleInput(uid, input);
    }

    @Override
    public synchronized void onSyncException(String uid, Exception e) throws Exception {
        IClientPlugin.super.onSyncException(uid, e);
        handleInput(uid, new MqProducerInputB(false, e.getMessage()));
    }

    @Override
    public synchronized void asyncSend(String topic, MqProducerMsgB msg, IMqMsgSendCallback callback) {
        buffer.computeIfAbsent(topic, t -> new Holder()).add(msg, callback);
    }

    // ***********************内部方法****************************

    private synchronized void handleInput(String uid, MqProducerInputB input) {
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
        final List<MqProducerMsgB> msgList = new ArrayList<>();
        final List<IMqMsgSendCallback> callbackList = new ArrayList<>();

        public void add(MqProducerMsgB msg, IMqMsgSendCallback callback) {
            msgList.add(msg);
            if (null != callback) {
                callbackList.add(callback);
            }
        }
    }

}
