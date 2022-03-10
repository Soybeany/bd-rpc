package com.soybeany.rpc.client.api;

import com.soybeany.sync.client.api.IClientPlugin;

import java.util.List;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public interface IRpcOtherPluginsProvider {

    static void setupExPlugins(String syncerId, List<IRpcOtherPluginsProvider> pluginProviders, List<IClientPlugin<?, ?>> plugins) {
        Optional.ofNullable(pluginProviders)
                .ifPresent(providers -> providers.forEach(provider -> plugins.addAll(provider.onSetupPlugins(syncerId))));
    }

    List<IClientPlugin<?, ?>> onSetupPlugins(String syncerId);

}
