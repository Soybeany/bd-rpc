package com.soybeany.rpc.consumer;

import com.soybeany.rpc.core.api.IRpcBatchInvoker;
import com.soybeany.rpc.core.api.IRpcClientService;
import com.soybeany.rpc.core.api.IRpcServiceProxy;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.sync.client.BaseClientSyncerImpl;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.picker.DataPicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseRpcRegistrySyncerImpl extends BaseClientSyncerImpl implements IRpcClientService, IRpcServiceProxy {

    @Autowired
    private ApplicationContext appContext;

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
        onSetupPkgPathToScan(paths);
        plugin = new RpcConsumerPlugin(onSetupSystem(), onSetupVersion(), appContext,
                this::onGetNewServerPicker,
                this::onSetupTimeoutInSec,
                paths
        );
        plugins.add(plugin);
    }

    // ***********************子类实现****************************

    protected String onSetupVersion() {
        return "0";
    }

    protected int onSetupTimeoutInSec(String serviceId) {
        return 5;
    }

    protected abstract DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId);

}
