package com.soybeany.rpc.core.plugin;

import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/29
 */
public abstract class BaseRpcAddScanPathPlugin implements IClientPlugin<Object, Object> {
    @Override
    public String onSetupSyncTagToHandle() {
        return null;
    }

    @Override
    public Class<Object> onGetInputClass() {
        return Object.class;
    }

    @Override
    public Class<Object> onGetOutputClass() {
        return Object.class;
    }

    @Override
    public boolean onBeforeSync(String uid, Object o) {
        return false;
    }

    @Override
    public void onPreTreat(List<IClientPlugin<Object, Object>> appliedPlugins) {
        IClientPlugin.super.onPreTreat(appliedPlugins);
        boolean hasMatchedPlugin = false;
        for (IClientPlugin<Object, Object> plugin : appliedPlugins) {
            if (!(plugin instanceof BaseRpcClientPlugin)) {
                continue;
            }
            hasMatchedPlugin = true;
            List<String> paths = new ArrayList<>();
            onSetupScanPaths(plugin, paths);
            ((BaseRpcClientPlugin<Object, Object>) plugin).addExPkgPathsToScan(paths);
        }
        if (!hasMatchedPlugin) {
            throw new RpcPluginException("缺少BaseRpcClientPlugin类型的插件");
        }
    }

    protected abstract void onSetupScanPaths(IClientPlugin<?, ?> plugin, List<String> paths);

}
