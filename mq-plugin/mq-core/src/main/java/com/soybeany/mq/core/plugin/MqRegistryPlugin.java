package com.soybeany.mq.core.plugin;

import com.soybeany.rpc.consumer.RpcConsumerPlugin;
import com.soybeany.rpc.core.plugin.BaseRpcAddScanPathPlugin;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/29
 */
public class MqRegistryPlugin extends BaseRpcAddScanPathPlugin {
    @Override
    protected void onSetupScanPaths(IClientPlugin<?, ?> plugin, List<String> paths) {
        if (plugin instanceof RpcConsumerPlugin) {
            paths.add("com.soybeany.mq.core.api");
        }
    }
}
