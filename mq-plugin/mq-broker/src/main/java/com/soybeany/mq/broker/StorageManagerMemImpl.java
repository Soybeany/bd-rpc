package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.MqConsumerMsg;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.core.model.MqTopicInfo;
import com.soybeany.sync.server.IAutoCleaner;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 需手工调用{@link #startAutoClean}与{@link #shutdownAutoClean}
 *
 * @author Soybeany
 * @date 2022/1/20
 */
public class StorageManagerMemImpl implements IStorageManager, IAutoCleaner {

    private static final int STATE_INVALID = -1;
    private static final int STATE_ACTIVE = 0;
    private static final int STATE_NOT_ACTIVE = 1;

    private final Map<String, TreeMap<Long, MqProducerMsg>> msgMap = new HashMap<>();
    private final Map<String, Long> stampMap = new HashMap<>();
    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    @Override
    public synchronized void save(Map<String, List<MqProducerMsg>> map) {
        map.forEach((topic, messages) -> {
            TreeMap<Long, MqProducerMsg> treeMap = msgMap.computeIfAbsent(topic, t -> new TreeMap<>());
            messages.forEach(msg -> {
                long stamp = Optional.ofNullable(stampMap.get(topic)).orElse(0L) + 1;
                stampMap.put(topic, stamp);
                treeMap.put(stamp, msg);
            });
        });
    }

    @Override
    public synchronized Map<String, MqConsumerMsg> load(List<MqTopicInfo> topics) {
        Map<String, MqConsumerMsg> result = new HashMap<>();
        long now = System.currentTimeMillis();
        topics.forEach(info -> Optional.ofNullable(msgMap.get(info.getTopic())).ifPresent(treeMap -> treeMap.tailMap(info.getStamp(), false).forEach((s, pMsg) -> {
            MqConsumerMsg cMsg = result.computeIfAbsent(info.getTopic(), t -> new MqConsumerMsg());
            cMsg.setStamp(s);
            if (STATE_ACTIVE == getState(now, pMsg)) {
                cMsg.getMessages().add(pMsg.getMsg());
            }
        })));
        return result;
    }

    @Override
    public void startAutoClean(long validPeriodInSec) {
        service.scheduleWithFixedDelay(() -> {
            long now = System.currentTimeMillis();
            synchronized (this) {
                msgMap.forEach((k, v) -> v.values().removeIf(msg -> STATE_INVALID == getState(now, msg)));
            }
        }, validPeriodInSec, validPeriodInSec, TimeUnit.SECONDS);
    }

    @Override
    public void shutdownAutoClean() {
        service.shutdown();
    }

    private int getState(long now, MqProducerMsg msg) {
        if (now < msg.getStartTime()) {
            return STATE_NOT_ACTIVE;
        }
        if (now > msg.getEndTime()) {
            return STATE_INVALID;
        }
        return STATE_ACTIVE;
    }

}
