package com.soybeany.mq.core.api;

import com.soybeany.mq.core.model.MqTopicExceptionInfo;
import com.soybeany.mq.core.model.MqTopicInfo;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcSerialize;

import java.util.Collection;

/**
 * 处理Mq回执
 *
 * @author Soybeany
 * @date 2022/2/21
 */
@BdRpc
public interface IMqReceiptHandler {

    void onSuccess(String ip, Collection<MqTopicInfo> topics);

    default void onException(String ip, @BdRpcSerialize Collection<MqTopicExceptionInfo> topics) {
    }
}
