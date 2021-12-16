package com.soybeany.rpc.provider;

import com.soybeany.rpc.core.api.ServiceInvoker;
import com.soybeany.rpc.core.model.BaseServiceImpl;
import com.soybeany.sync.client.SyncClientService;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.util.NetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceInvokeImpl extends BaseServiceImpl implements ServiceInvoker {

    @Autowired
    private ApplicationContext appContext;

    private RpcProviderPlugin plugin;
    private SyncClientService service;

    @Override
    public SyncDTO invoke(HttpServletRequest request, HttpServletResponse response) {
        return plugin.invoke(request, response);
    }

    @Override
    protected void onStart() {
        String syncUrl = onSetupServerSyncUrl(NetUtils.getLocalIpAddress());
        plugin = new RpcProviderPlugin(onSetupSystem(), appContext, syncUrl, onSetupPkgPathToScan()).init();
        service = new SyncClientService(this, plugin);
        service.start();
    }

    @Override
    protected void onStop() {
        service.stop();
    }

    @SuppressWarnings("SameParameterValue")
    protected String getUrl(boolean secure, String ip, int port, String context, String path, String suffix) {
        String protocol = secure ? "https" : "http";
        return protocol + "://" + ip + ":" + port + context + path + suffix;
    }

    // ***********************子类实现****************************

    @NonNull
    protected abstract String onSetupServerSyncUrl(String ip);

}
