package com.soybeany.mq.client.plugin;

import com.soybeany.rpc.client.plugin.BaseRpcAddScanPathPlugin;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.sync.client.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/29
 */
public abstract class BaseMqClientRegistryPlugin extends BaseRpcAddScanPathPlugin {
    @Override
    protected void onAddScanPaths(IClientPlugin<?, ?> plugin, List<String> paths) {
        if (plugin instanceof RpcConsumerPlugin) {
            paths.add("com.soybeany.mq.core.api");
        }
    }
}
