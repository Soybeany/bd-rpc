package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqMsgAsyncSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.exception.MqPluginInterruptException;
import com.soybeany.mq.core.exception.MqPluginTimeoutException;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqProducerInput;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.core.model.MqProducerOutput;
import com.soybeany.sync.core.api.IClientPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Slf4j
public class MqProducerPlugin implements IClientPlugin<MqProducerInput, MqProducerOutput>, IMqMsgSender {

    private static final int DEFAULT_TIMEOUT = 300;

    private final List<MqProducerMsg> msgBuffer = new ArrayList<>();
    private final Set<IMqMsgAsyncSendCallback> callbackBuffer = new HashSet<>();
    private final Map<String, Archive> archiveMap = new HashMap<>();
    private int syncIntervalInSec = DEFAULT_TIMEOUT;
    private MqProducerInput lastInput;
    private CountDownLatch syncLatch = getNewSyncLatch();
    private CountDownLatch callbackLatch;
    private int syncCount;

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
    public void onStartup(int syncIntervalInSec) {
        IClientPlugin.super.onStartup(syncIntervalInSec);
        this.syncIntervalInSec = syncIntervalInSec;
    }

    @Override
    public synchronized void onHandleOutput(String uid, MqProducerOutput output) throws Exception {
        IClientPlugin.super.onHandleOutput(uid, output);
        // 处理消息缓冲
        output.getMessages().addAll(msgBuffer);
        msgBuffer.clear();
        // 归档
        archiveMap.put(uid, new Archive(syncLatch, syncCount, callbackBuffer));
        syncCount = 0;
        callbackBuffer.clear();
        // 使用新的锁
        syncLatch = getNewSyncLatch();
    }

    @Override
    public synchronized void onHandleInput(String uid, MqProducerInput input) throws Exception {
        IClientPlugin.super.onHandleInput(uid, input);
        handleInput(uid, input);
    }

    @Override
    public synchronized void onHandleException(String uid, Exception e) throws Exception {
        IClientPlugin.super.onHandleException(uid, e);
        handleInput(uid, new MqProducerInput(false, e.getMessage()));
    }

    @Override
    public void syncSend(MqProducerMsg msg) throws MqPluginException {
        // 消息写入到缓冲
        CountDownLatch latch;
        synchronized (this) {
            msgBuffer.add(msg);
            syncCount++;
            latch = syncLatch;
        }
        // 等待消息发送
        awaitLatch("mq消息发送", latch, syncIntervalInSec);
        MqProducerInput lastInput = this.lastInput;
        callbackLatch.countDown();
        // 处理消息发送结果
        if (!lastInput.isSuccess()) {
            throw new MqPluginException(lastInput.getMsg());
        }
    }

    @Override
    public synchronized void asyncSend(MqProducerMsg msg, IMqMsgAsyncSendCallback callback) {
        msgBuffer.add(msg);
        if (null != callback) {
            callbackBuffer.add(callback);
        }
    }

    // ***********************内部方法****************************

    private synchronized void handleInput(String uid, MqProducerInput input) throws Exception {
        Archive archive = archiveMap.remove(uid);
        if (null == archive) {
            log.warn("无法找到uid(" + uid + ")的归档");
            return;
        }
        lastInput = input;
        // 解锁
        callbackLatch = new CountDownLatch(archive.syncCount);
        archive.latch.countDown();
        // 调用异步回调
        for (IMqMsgAsyncSendCallback callback : archive.callbacks) {
            try {
                callback.onFinish(input);
            } catch (Exception ignore) {
            }
        }
        // 等待同步回调
        awaitLatch("同步回调处理", callbackLatch, 10);
    }

    private void awaitLatch(String desc, CountDownLatch latch, int timeoutInSec) throws MqPluginException {
        try {
            if (!latch.await(timeoutInSec, TimeUnit.SECONDS)) {
                throw new MqPluginTimeoutException(desc + "等待超时");
            }
        } catch (InterruptedException e) {
            throw new MqPluginInterruptException(desc + "等待中断");
        }
    }

    private CountDownLatch getNewSyncLatch() {
        return new CountDownLatch(1);
    }

    // ***********************内部类****************************

    private static class Archive {
        final CountDownLatch latch;
        final int syncCount;
        final Set<IMqMsgAsyncSendCallback> callbacks;

        public Archive(CountDownLatch latch, int syncCount, Set<IMqMsgAsyncSendCallback> callbacks) {
            this.latch = latch;
            this.syncCount = syncCount;
            this.callbacks = new HashSet<>(callbacks);
        }
    }

}
