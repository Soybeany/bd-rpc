package com.soybeany.rpc.consumer;

import com.soybeany.rpc.core.api.IRpcClientService;
import com.soybeany.rpc.core.api.IServiceProxy;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.picker.DataPicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceProxyImpl extends BaseClientServiceImpl implements IRpcClientService, IServiceProxy {

    @Autowired
    private ApplicationContext appContext;

    private RpcConsumerPlugin plugin;

    @Override
    public <T> T get(Class<T> interfaceClass) throws RpcPluginException {
        return plugin.get(interfaceClass);
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugin = new RpcConsumerPlugin(onSetupSystem(), appContext,
                this::onGetNewServerPicker,
                this::onSetupTimeoutInSec,
                onSetupPkgPathToScan()
        );
        plugin.init();
        plugins.add(plugin);
    }

    // ***********************子类实现****************************

    protected int onSetupTimeoutInSec(String serviceId) {
        return 5;
    }

    protected abstract DataPicker<ServerInfo> onGetNewServerPicker(String serviceId);

}
