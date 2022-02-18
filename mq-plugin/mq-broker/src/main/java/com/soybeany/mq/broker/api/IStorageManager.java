package com.soybeany.mq.broker.api;

import com.soybeany.mq.core.model.MqConsumerMsg;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.core.model.MqTopicInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
public interface IStorageManager {

    void save(Map<String, List<MqProducerMsg>> messages) throws Exception;

    Map<String, MqConsumerMsg> load(List<MqTopicInfo> topics) throws Exception;

}
