package com.soybeany.rpc.client.api;

import com.soybeany.sync.client.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public interface IRpcOtherPluginsProvider {

    List<IClientPlugin<?, ?>> onSetupPlugins();

}
