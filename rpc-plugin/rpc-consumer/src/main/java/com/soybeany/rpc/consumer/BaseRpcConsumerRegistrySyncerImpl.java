package com.soybeany.rpc.consumer;

import com.soybeany.rpc.consumer.api.IRpcBatchInvoker;
import com.soybeany.rpc.consumer.api.IRpcExApiPkgProvider;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
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
public abstract class BaseRpcConsumerRegistrySyncerImpl extends BaseClientSyncerImpl implements IRpcServiceProxy {

    @Autowired(required = false)
    private List<IRpcExApiPkgProvider> providers;

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
        Set<String> paths = new HashSet<>();
        onSetupApiPkgToScan(paths);
        Optional.ofNullable(providers)
                .ifPresent(providers -> providers.forEach(provider -> provider.onSetupApiPkgToScan(paths)));
        plugin = new RpcConsumerPlugin(this::onGetNewServerPicker, this::onSetupInvokeTimeoutSec, paths);
        plugins.add(plugin);
    }

    // ***********************子类实现****************************

    protected int onSetupInvokeTimeoutSec(String serviceId) {
        return 5;
    }

    /**
     * 为特定的service配置选择器
     */
    protected abstract DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId);

    /**
     * “接口” 所在路径
     */
    protected abstract void onSetupApiPkgToScan(Set<String> paths);

}
