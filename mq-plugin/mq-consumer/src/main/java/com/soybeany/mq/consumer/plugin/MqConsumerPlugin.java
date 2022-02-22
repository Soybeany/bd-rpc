package com.soybeany.mq.consumer.plugin;

import com.soybeany.mq.client.plugin.BaseMqClientRegistryPlugin;
import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.IMqMsgHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import com.soybeany.mq.core.api.IMqMsgStorageManager;
import com.soybeany.mq.core.api.IMqReceiptHandler;
import com.soybeany.mq.core.model.MqConsumerMsg;
import com.soybeany.mq.core.model.MqTopicExceptionInfo;
import com.soybeany.mq.core.model.MqTopicInfo;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import com.soybeany.sync.client.model.SyncClientInfo;
import com.soybeany.sync.core.util.NetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Soybeany
 * @date 2022/2/21
 */
@Slf4j
@RequiredArgsConstructor
public class MqConsumerPlugin extends BaseMqClientRegistryPlugin {

    private static final IMqReceiptHandler EMPTY_HANDLER = (ip, info) -> {
    };

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    /**
     * 设置拉取消息的间隔（单位：秒）
     */
    private final int pullIntervalSec;
    private final IRpcServiceProxy proxy;
    private final List<IMqMsgHandler<? extends Serializable>> handlers;
    private final ITopicInfoRepository repository;
    private final IMqExceptionHandler exceptionHandler;
    private final boolean enableReceipt;

    private IMqMsgStorageManager mqMsgStorageManager;
    private IMqReceiptHandler mqReceiptHandler = EMPTY_HANDLER;
    private Map<String, IMqMsgHandler<Serializable>> msgHandlerMap;

    // ***********************公开方法****************************

    @Override
    public void onStartup(SyncClientInfo info) {
        super.onStartup(info);
        // 变量赋值
        mqMsgStorageManager = proxy.get(IMqMsgStorageManager.class);
        if (enableReceipt) {
            mqReceiptHandler = proxy.get(IMqReceiptHandler.class);
        }
        msgHandlerMap = toMap(handlers);
        // 执行定时任务
        service.scheduleWithFixedDelay(this::safePull, 2, pullIntervalSec, TimeUnit.SECONDS);
    }

    @Override
    public void onShutdown() {
        super.onShutdown();
        service.shutdown();
    }

    // ***********************内部方法****************************

    private void safePull() {
        try {
            onPull();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void onPull() {
        // 拉取消息
        Set<String> topics = msgHandlerMap.keySet();
        Collection<MqTopicInfo> infoList = repository.getAll(topics);
        Map<String, MqConsumerMsg<Serializable>> msgMap;
        try {
            msgMap = mqMsgStorageManager.load(infoList);
        } catch (Exception e) {
            return;
        }
        // 处理消息
        handleAllMsg(infoList, msgMap);
    }

    private void handleAllMsg(Collection<MqTopicInfo> infoList, Map<String, MqConsumerMsg<Serializable>> msgMap) {
        List<MqTopicInfo> successList = new ArrayList<>();
        List<MqTopicExceptionInfo> failureList = new ArrayList<>();
        Map<String, Long> oldStampMap = new HashMap<>();
        // 循环处理每个消息
        msgMap.forEach((topic, msg) -> handleOneMsg(infoList, oldStampMap, successList, failureList, topic, msg));
        // 执行回调
        String ip = NetUtils.getLocalIpAddress();
        if (!successList.isEmpty()) {
            mqReceiptHandler.onSuccess(ip, successList);
        }
        if (!failureList.isEmpty()) {
            mqReceiptHandler.onException(ip, failureList);
        }
    }

    private void handleOneMsg(Collection<MqTopicInfo> infoList, Map<String, Long> oldStampMap, List<MqTopicInfo> successList,
                              List<MqTopicExceptionInfo> failureList, String topic, MqConsumerMsg<Serializable> msg) {
        // 数据分发
        boolean enableUpdate = true;
        Exception exception = null;
        List<Serializable> msgList = msg.getMsgList();
        try {
            if (!msgList.isEmpty()) {
                msgHandlerMap.get(topic).onHandle(msgList);
            }
        } catch (Exception e) {
            exception = e;
            if (oldStampMap.isEmpty()) {
                infoList.forEach(info -> oldStampMap.put(info.getTopic(), info.getStamp()));
            }
            enableUpdate = exceptionHandler.onException(topic, e, oldStampMap.get(topic), msg.getStamp(), msgList);
        }
        MqTopicInfo curTopicInfo = new MqTopicInfo(topic, msg.getStamp());
        // 更新topic的戳
        if (enableUpdate) {
            try {
                repository.updateTopicInfo(curTopicInfo);
            } catch (Exception e) {
                exception = e;
            }
        }
        // 记录信息
        if (null != exception) {
            failureList.add(new MqTopicExceptionInfo(topic, msg.getStamp(), exception));
        } else {
            successList.add(curTopicInfo);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, IMqMsgHandler<Serializable>> toMap(List<IMqMsgHandler<? extends Serializable>> handlers) {
        Map<String, IMqMsgHandler<Serializable>> map = new HashMap<>();
        for (IMqMsgHandler handler : handlers) {
            IMqMsgHandler previous = map.put(handler.onSetupTopic(), handler);
            if (null != previous) {
                throw new RuntimeException("同一个主题“" + handler.onSetupTopic() + "”，只能有一个IMqMsgHandler实现");
            }
        }
        return map;
    }

}
