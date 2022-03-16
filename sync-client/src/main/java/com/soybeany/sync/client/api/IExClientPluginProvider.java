package com.soybeany.sync.client.api;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/3/16
 */
public interface IExClientPluginProvider {

    void onSetupPlugins(String syncerId, List<IClientPlugin<?, ?>> plugins);

}
