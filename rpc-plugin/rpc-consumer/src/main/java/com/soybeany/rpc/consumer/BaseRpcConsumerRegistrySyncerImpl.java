package com.soybeany.rpc.consumer;

import com.soybeany.rpc.client.api.IRpcOtherPluginsProvider;
import com.soybeany.rpc.consumer.api.IRpcBatchInvoker;
import com.soybeany.rpc.consumer.api.IRpcExApiPkgProvider;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import com.soybeany.rpc.consumer.api.IRpcServiceProxyAware;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import com.soybeany.sync.client.picker.DataPicker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public abstract class BaseRpcConsumerRegistrySyncerImpl extends BaseClientSyncerImpl implements IRpcServiceProxy {

    private static final int DEFAULT_INVOKE_TIMEOUT = 5;

    @Autowired(required = false)
    private List<IRpcExApiPkgProvider> apiProviders;
    @Autowired(required = false)
    private List<IRpcOtherPluginsProvider> pluginProviders;

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
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        // 添加插件
        Set<String> paths = new HashSet<>();
        onSetupApiPkgToScan(paths);
        Optional.ofNullable(apiProviders)
                .ifPresent(providers -> providers.forEach(provider -> provider.onSetupApiPkgToScan(paths)));
        plugin = new RpcConsumerPlugin(this::onGetNewServerPicker, this::onSetupInvokeTimeoutSec, paths);
        plugins.add(plugin);
        Optional.ofNullable(pluginProviders)
                .ifPresent(providers -> providers.forEach(provider -> plugins.addAll(provider.onSetupPlugins())));
        // 设置子类插件
        onSetupOtherPlugins(plugins);
        // 调用发现回调
        for (IClientPlugin<?, ?> plugin : plugins) {
            if (plugin instanceof IRpcServiceProxyAware) {
                ((IRpcServiceProxyAware) plugin).onSetupRpcServiceProxy(this);
            }
        }
    }

    // ***********************子类实现****************************

    protected int onSetupInvokeTimeoutSec(String serviceId) {
        return DEFAULT_INVOKE_TIMEOUT;
    }

    /**
     * 为特定的service配置选择器
     */
    protected abstract DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId);

    /**
     * “接口” 所在路径
     */
    protected abstract void onSetupApiPkgToScan(Set<String> paths);

    /**
     * 允许子类设置其它插件
     */
    protected abstract void onSetupOtherPlugins(List<IClientPlugin<?, ?>> plugins);

}
