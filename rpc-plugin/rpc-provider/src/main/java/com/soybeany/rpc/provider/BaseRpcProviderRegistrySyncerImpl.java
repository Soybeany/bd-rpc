package com.soybeany.rpc.provider;

import com.soybeany.rpc.provider.api.IRpcServiceExecutor;
import com.soybeany.rpc.provider.plugin.RpcProviderPlugin;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
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
public abstract class BaseRpcProviderRegistrySyncerImpl extends BaseClientSyncerImpl implements IRpcServiceExecutor {

    private RpcProviderPlugin plugin;

    @Override
    public SyncDTO execute(HttpServletRequest request, HttpServletResponse response) {
        return plugin.execute(request, response);
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        Set<String> paths = new HashSet<>();
        onSetupImplPkgToScan(paths);
        plugin = new RpcProviderPlugin(onSetupGroup(), onSetupInvokeUrl(NetUtils.getLocalIpAddress()), paths);
        plugins.add(plugin);
    }

    // ***********************子类实现****************************

    /**
     * 配置分组<br/>
     * 与{@link #onSetupSystem()}的静态硬隔离不同，这是动态的软隔离
     */
    protected String onSetupGroup() {
        return null;
    }

    /**
     * 配置对外暴露服务所使用的url
     */
    @NonNull
    protected abstract String onSetupInvokeUrl(String ip);

    /**
     * “实现类” 所在路径
     */
    protected abstract void onSetupImplPkgToScan(Set<String> paths);

}
