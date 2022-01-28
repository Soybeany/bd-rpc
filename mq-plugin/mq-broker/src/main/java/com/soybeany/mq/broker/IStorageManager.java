package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.MqConsumerMsg;
import com.soybeany.mq.core.model.MqProducerMsg;

import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
public interface IStorageManager {

    void save(Map<String, List<MqProducerMsg>> messages) throws Exception;

    Map<String, MqConsumerMsg> load(Map<String, Long> topics) throws Exception;

}
