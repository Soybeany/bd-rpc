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
import com.soybeany.sync.client.api.ISystemConfig;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.util.NetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
public abstract class BaseRpcUnitRegistrySyncerImpl extends BaseClientSyncerImpl implements ISystemConfig, IRpcServiceProxy, IRpcServiceExecutor {

    @Autowired
    protected ApplicationContext appContext;

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
        consumerPlugin = new RpcConsumerPlugin(onSetupSystem(), onSetupVersion(), appContext,
                this::onGetNewServerPicker,
                this::onSetupTimeoutInSec,
                apiPaths
        );
        plugins.add(consumerPlugin);
        // 设置生产者插件
        String invokeUrl = onSetupInvokeUrl(NetUtils.getLocalIpAddress());
        Set<String> paths = new HashSet<>();
        onSetupImplPkgToScan(paths);
        providerPlugin = new RpcProviderPlugin(onSetupSystem(), onSetupVersion(), onSetupGroup(), appContext, invokeUrl, paths);
        plugins.add(providerPlugin);
    }

    // ***********************子类实现****************************

    protected String onSetupVersion() {
        return "0";
    }

    protected int onSetupTimeoutInSec(String serviceId) {
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
