package com.soybeany.mq.core.api;

import com.soybeany.mq.core.model.MqReceiptInfo;
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

    void onSuccess(String ip, Collection<MqReceiptInfo> info);

    default void onException(String ip, @BdRpcSerialize Collection<MqReceiptInfo.WithE> info) {
    }

}
