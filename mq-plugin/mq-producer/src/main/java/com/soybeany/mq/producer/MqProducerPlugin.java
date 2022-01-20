package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqMsgSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqProducerInput;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.core.model.MqProducerOutput;
import com.soybeany.sync.core.api.IClientPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Slf4j
public class MqProducerPlugin implements IClientPlugin<MqProducerInput, MqProducerOutput>, IMqMsgSender {

    private final Map<String, List<MqProducerMsg>> msgBuffer = new HashMap<>();
    private final Set<IMqMsgSendCallback> callbackBuffer = new HashSet<>();
    private final Map<String, Archive> archiveMap = new HashMap<>();

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
        if (msgBuffer.isEmpty()) {
            return false;
        }
        // 处理消息缓冲
        output.getMessages().putAll(msgBuffer);
        msgBuffer.clear();
        // 归档
        archiveMap.put(uid, new Archive(callbackBuffer));
        callbackBuffer.clear();

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
    public synchronized void send(String topic, MqProducerMsg msg, IMqMsgSendCallback callback) {
        addMsg(topic, msg);
        if (null != callback) {
            callbackBuffer.add(callback);
        }
    }

    // ***********************内部方法****************************

    private synchronized void handleInput(String uid, MqProducerInput input) {
        log.warn("调用处理");
        Archive archive = archiveMap.remove(uid);
        if (null == archive) {
            log.warn("无法找到uid(" + uid + ")的归档");
            return;
        }
        // 调用异步回调
        for (IMqMsgSendCallback callback : archive.callbacks) {
            try {
                callback.onFinish(input);
            } catch (Exception ignore) {
            }
        }
    }

    private void addMsg(String topic, MqProducerMsg msg) {
        msgBuffer.computeIfAbsent(topic, t -> new ArrayList<>()).add(msg);
    }

    // ***********************内部类****************************

    private static class Archive {
        final Set<IMqMsgSendCallback> callbacks;

        public Archive(Set<IMqMsgSendCallback> callbacks) {
            this.callbacks = new HashSet<>(callbacks);
        }
    }

}
