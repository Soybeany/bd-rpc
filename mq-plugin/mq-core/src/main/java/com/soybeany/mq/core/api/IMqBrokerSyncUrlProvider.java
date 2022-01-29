package com.soybeany.mq.core.api;

import com.soybeany.rpc.core.anno.BdCache;
import com.soybeany.rpc.core.anno.BdRpc;

/**
 * @author Soybeany
 * @date 2022/1/28
 */
@BdRpc(serviceId = "bd-mq")
public interface IMqBrokerSyncUrlProvider {

    @BdCache(desc = "同步", needLog = false, fastFailExpiry = 10 * 1000)
    String onGetSyncUrl();

}
