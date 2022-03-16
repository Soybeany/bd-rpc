package com.soybeany.rpc.consumer;

import com.soybeany.rpc.consumer.api.*;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public abstract class BaseRpcConsumerRegistrySyncerImpl extends BaseClientSyncerImpl implements IRpcServiceProxy, IRpcConsumerSyncer {

    @Autowired(required = false)
    private List<IRpcExApiPkgProvider> apiPkgProviders;

    private RpcConsumerPlugin plugin;

    @Override
    public <T> T get(Class<T> interfaceClass) throws RpcPluginException {
        return plugin.get(interfaceClass);
    }

    @Override
    public <T> IRpcBatchInvoker<T> getBatch(Class<?> interfaceClass, String methodId) {
        return plugin.getBatch(interfaceClass, methodId);
    }

    @Override
    protected void onSetupPlugins(String syncerId, List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = IRpcConsumerSyncer.getRpcConsumerPlugin(syncerId, this, apiPkgProviders));
    }

    @Override
    protected void postSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.postSetupPlugins(plugins);
        IRpcServiceProxyAware.invokeRpcServiceProxyAware(plugins, this);
    }

}
