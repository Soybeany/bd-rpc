package com.soybeany.rpc.unit;

import com.soybeany.rpc.consumer.api.IRpcBatchInvoker;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import com.soybeany.rpc.consumer.model.RpcProxySelector;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.rpc.provider.api.IRpcServiceExecutor;
import com.soybeany.rpc.provider.plugin.RpcProviderPlugin;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.util.NetUtils;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseRpcUnitRegistrySyncerImpl extends BaseClientSyncerImpl implements IRpcServiceProxy, IRpcServiceExecutor {

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
        Set<String> apiPaths = new HashSet<>();
        onSetupApiPkgToScan(apiPaths);
        consumerPlugin = new RpcConsumerPlugin(this::onGetNewServerPicker, this::onSetupInvokeTimeoutSec, apiPaths);
        plugins.add(consumerPlugin);
        // 设置生产者插件
        Set<String> paths = new HashSet<>();
        onSetupImplPkgToScan(paths);
        providerPlugin = new RpcProviderPlugin(onSetupGroup(), onSetupInvokeUrl(NetUtils.getLocalIpAddress()), paths);
        plugins.add(providerPlugin);
    }

    // ***********************子类实现****************************

    protected int onSetupInvokeTimeoutSec(String serviceId) {
        return 5;
    }

    /**
     * 配置分组<br/>
     * 与{@link #onSetupSystem()}的静态硬隔离不同，这是动态的软隔离
     * 与{@link RpcProxySelector#get}的入参group对应
     */
    protected String onSetupGroup() {
        return null;
    }

    /**
     * 为特定的service配置选择器
     */
    protected abstract DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId);

    /**
     * 配置对外暴露服务所使用的url
     */
    @NonNull
    protected abstract String onSetupInvokeUrl(String ip);

    /**
     * “接口” 所在路径
     */
    protected abstract void onSetupApiPkgToScan(Set<String> paths);

    /**
     * “实现类” 所在路径
     */
    protected abstract void onSetupImplPkgToScan(Set<String> paths);

}
