package com.soybeany.rpc.consumer.api;

import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.sync.client.picker.DataPicker;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/3/10
 */
public interface IRpcConsumerSyncer {

    int DEFAULT_INVOKE_TIMEOUT = 5;

    static RpcConsumerPlugin getRpcConsumerPlugin(String syncerId, IRpcConsumerSyncer syncer, List<IRpcExApiPkgProvider> apiPkgProviders) {
        Set<String> paths = new HashSet<>();
        syncer.onSetupApiPkgToScan(paths);
        Optional.ofNullable(apiPkgProviders)
                .ifPresent(providers -> providers.forEach(provider -> provider.onSetupApiPkgToScan(paths)));
        return new RpcConsumerPlugin(syncerId, syncer::onGetNewServerPicker, syncer::onSetupInvokeTimeoutSec, paths);
    }

    default int onSetupInvokeTimeoutSec(String serviceId) {
        return DEFAULT_INVOKE_TIMEOUT;
    }

    /**
     * 为特定的service配置选择器
     */
    DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId);

    /**
     * “接口” 所在路径
     */
    void onSetupApiPkgToScan(Set<String> paths);

}
