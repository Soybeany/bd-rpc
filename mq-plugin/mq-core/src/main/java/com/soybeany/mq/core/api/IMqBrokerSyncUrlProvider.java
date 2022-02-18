package com.soybeany.mq.core.api;

import com.soybeany.rpc.client.anno.BdRpc;
import com.soybeany.rpc.consumer.anno.BdRpcCache;

/**
 * @author Soybeany
 * @date 2022/1/28
 */
@BdRpc(serviceId = "bd-mq-broker-sync-url-provider")
public interface IMqBrokerSyncUrlProvider {

    @BdRpcCache(desc = "同步", needLog = false, ttl = 60, ttlErr = 10)
    String onGetSyncUrl();

}
