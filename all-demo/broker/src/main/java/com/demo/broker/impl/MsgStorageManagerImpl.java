package com.demo.broker.impl;

import com.soybeany.mq.broker.impl.MqMsgStorageManagerMemImpl;
import com.soybeany.mq.core.api.IMqMsgStorageManager;
import com.soybeany.mq.core.model.MqConsumerMsg;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.core.model.MqTopicInfo;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Component
public class MsgStorageManagerImpl implements IMqMsgStorageManager {

    private final IMqMsgStorageManager target = new MqMsgStorageManagerMemImpl();

    @Override
    public <T extends Serializable> void save(String topic, MqProducerMsg<T> msg) {
        target.save(topic, msg);
    }

    @Override
    public <T extends Serializable> Map<String, MqConsumerMsg<T>> load(Collection<MqTopicInfo> topics) {
        return target.load(topics);
    }

}
