package com.soybeany.rpc.provider.api;

import com.soybeany.rpc.provider.plugin.RpcProviderPlugin;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import com.soybeany.sync.core.util.NetUtils;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/3/10
 */
public interface IRpcProviderSyncer {

    static RpcProviderPlugin getRpcProviderPlugin(IRpcProviderSyncer syncer, List<IRpcExImplPkgProvider> implPkgProviders) {
        Set<String> paths = new HashSet<>();
        syncer.onSetupImplPkgToScan(paths);
        Optional.ofNullable(implPkgProviders)
                .ifPresent(providers -> providers.forEach(provider -> provider.onSetupImplPkgToScan(paths)));
        return new RpcProviderPlugin(syncer.onSetupGroup(), syncer.onSetupInvokeUrl(NetUtils.getLocalIpAddress()), paths);
    }

    /**
     * 配置分组<br/>
     * 与{@link BaseClientSyncerImpl#onSetupSystem}的静态硬隔离不同，这是动态的软隔离
     */
    @SuppressWarnings("JavadocReference")
    default String onSetupGroup() {
        return null;
    }

    /**
     * 配置对外暴露服务所使用的url
     */
    @NonNull
    String onSetupInvokeUrl(String ip);

    /**
     * “实现类” 所在路径
     */
    void onSetupImplPkgToScan(Set<String> paths);

}
