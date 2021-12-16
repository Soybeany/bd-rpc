package com.soybeany.rpc.consumer;

import com.soybeany.rpc.core.api.ServiceProxy;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.BaseClientServiceImpl;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.client.SyncClientService;
import com.soybeany.sync.core.picker.DataPicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceProxyImpl extends BaseClientServiceImpl implements ServiceProxy {

    @Autowired
    private ApplicationContext appContext;

    private RpcConsumerPlugin plugin;
    private SyncClientService service;

    @Override
    public <T> T get(Class<T> interfaceClass) throws RpcPluginException {
        return plugin.get(interfaceClass);
    }

    @Override
    protected void onStart() {
        super.onStart();
        plugin = new RpcConsumerPlugin(onSetupSystem(), appContext, this::onGetNewDataPicker, onSetupPkgPathToScan()).init();
        service = new SyncClientService(this, plugin);
        service.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        service.stop();
    }

    // ***********************子类实现****************************

    protected abstract DataPicker<ServerInfo> onGetNewDataPicker(String serviceId);

}
