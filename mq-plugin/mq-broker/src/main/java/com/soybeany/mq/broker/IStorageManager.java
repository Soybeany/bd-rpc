package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.MqConsumerMsgB;
import com.soybeany.mq.core.model.MqProducerMsgB;

import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
public interface IStorageManager {

    void save(Map<String, List<MqProducerMsgB>> messages) throws Exception;

    Map<String, MqConsumerMsgB> load(Map<String, Long> topics) throws Exception;

}
