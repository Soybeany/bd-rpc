package com.soybeany.rpc.provider;

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
public abstract class BaseRpcProviderRegistrySyncerImpl extends BaseClientSyncerImpl implements IRpcServiceExecutor, IRpcProviderSyncer {

    @Autowired(required = false)
    private List<IRpcExImplPkgProvider> implPkgProviders;

    private RpcProviderPlugin plugin;

    @Override
    public SyncDTO execute(HttpServletRequest request, HttpServletResponse response) {
        return plugin.execute(request, response);
    }

    @Override
    protected void onSetupPlugins(String syncerId, List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = IRpcProviderSyncer.getRpcProviderPlugin(this, implPkgProviders));
    }

}
