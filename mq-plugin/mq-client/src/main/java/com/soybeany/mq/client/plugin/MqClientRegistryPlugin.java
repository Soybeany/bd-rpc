package com.soybeany.mq.client.plugin;

import com.soybeany.rpc.client.plugin.BaseRpcAddScanPathPlugin;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.sync.client.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/29
 */
public class MqClientRegistryPlugin extends BaseRpcAddScanPathPlugin {
    @Override
    protected void onSetupScanPaths(IClientPlugin<?, ?> plugin, List<String> paths) {
        if (plugin instanceof RpcConsumerPlugin) {
            paths.add("com.soybeany.mq.core.api");
        }
    }
}
