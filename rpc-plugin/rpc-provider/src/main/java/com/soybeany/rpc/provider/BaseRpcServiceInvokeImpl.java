package com.soybeany.rpc.provider;

import com.soybeany.rpc.core.api.IRpcClientService;
import com.soybeany.rpc.core.api.IRpcServiceInvoker;
import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.util.NetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseRpcServiceInvokeImpl extends BaseClientServiceImpl implements IRpcClientService, IRpcServiceInvoker {

    @Autowired
    private ApplicationContext appContext;

    private RpcProviderPlugin plugin;

    @Override
    public SyncDTO invoke(HttpServletRequest request, HttpServletResponse response) {
        return plugin.invoke(request, response);
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        String invokeUrl = onSetupInvokeUrl(NetUtils.getLocalIpAddress());
        plugin = new RpcProviderPlugin(onSetupSystem(), onSetupVersion(), onSetupTag(), appContext, invokeUrl, onSetupPkgPathToScan());
        plugins.add(plugin);
    }

    // ***********************子类实现****************************

    protected String onSetupVersion() {
        return "0";
    }

    protected String onSetupTag() {
        return null;
    }

    @NonNull
    protected abstract String onSetupInvokeUrl(String ip);

}
