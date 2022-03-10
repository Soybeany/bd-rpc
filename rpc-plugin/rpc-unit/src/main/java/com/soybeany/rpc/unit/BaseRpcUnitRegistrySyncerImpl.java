package com.soybeany.rpc.unit;

import com.soybeany.rpc.client.api.IRpcOtherPluginsProvider;
import com.soybeany.rpc.consumer.api.*;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.provider.api.IRpcExImplPkgProvider;
import com.soybeany.rpc.provider.api.IRpcProviderSyncer;
import com.soybeany.rpc.provider.api.IRpcServiceExecutor;
import com.soybeany.rpc.provider.plugin.RpcProviderPlugin;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import com.soybeany.sync.core.model.SyncDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseRpcUnitRegistrySyncerImpl extends BaseClientSyncerImpl
        implements IRpcServiceProxy, IRpcServiceExecutor, IRpcConsumerSyncer, IRpcProviderSyncer {

    @Autowired(required = false)
    private List<IRpcExApiPkgProvider> apiPkgProviders;
    @Autowired(required = false)
    private List<IRpcExImplPkgProvider> implPkgProviders;
    @Autowired(required = false)
    private List<IRpcOtherPluginsProvider> pluginProviders;

    private RpcConsumerPlugin consumerPlugin;
    private RpcProviderPlugin providerPlugin;

    @Override
    public <T> T get(Class<T> interfaceClass) throws RpcPluginException {
        return consumerPlugin.get(interfaceClass);
    }

    @Override
    public <T> IRpcBatchInvoker<T> getBatch(Class<?> interfaceClass, String methodId) {
        return consumerPlugin.getBatch(interfaceClass, methodId);
    }

    @Override
    public SyncDTO execute(HttpServletRequest request, HttpServletResponse response) {
        return providerPlugin.execute(request, response);
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        // 设置消费者插件
        plugins.add(consumerPlugin = IRpcConsumerSyncer.getRpcConsumerPlugin(this, apiPkgProviders));
        // 设置生产者插件
        plugins.add(providerPlugin = IRpcProviderSyncer.getRpcProviderPlugin(this, implPkgProviders));
        // 设置额外的插件
        IRpcOtherPluginsProvider.setupExPlugins(onSetupSyncerId(), pluginProviders, plugins);
    }

    @Override
    protected void postSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.postSetupPlugins(plugins);
        IRpcServiceProxyAware.invokeRpcServiceProxyAware(plugins, this);
    }
}
