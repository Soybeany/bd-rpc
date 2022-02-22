package com.soybeany.mq.core.api;

import com.soybeany.mq.core.model.MqConsumerMsg;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.core.model.MqTopicInfo;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcSerialize;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@BdRpc
public interface IMqMsgStorageManager {

    @BdRpcSerialize
    <T extends Serializable> void save(String topic, @BdRpcSerialize MqProducerMsg<T> msg);

    @BdRpcSerialize
    <T extends Serializable> Map<String, MqConsumerMsg<T>> load(Collection<MqTopicInfo> topics);

}
