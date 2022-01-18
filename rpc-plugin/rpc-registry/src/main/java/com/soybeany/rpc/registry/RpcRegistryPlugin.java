package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.BaseRpcClientOutput;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.exception.SyncException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Soybeany
 * @date 2022/1/17
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RpcRegistryPlugin<Input extends BaseRpcClientOutput, Output> implements IServerPlugin<Input, Output> {

    private final Map<String, IServiceManager> serviceManagerMap;

    public static List<IServerPlugin<?, ?>> get(String[] acceptableSystems, Function<String, IServiceManager> serviceManagerProvider) {
        Map<String, IServiceManager> serviceManagerMap = getServiceManagerMap(acceptableSystems, serviceManagerProvider);
        RpcRegistryPlugin<?, ?> pluginC = new RpcRegistryPluginC(serviceManagerMap);
        RpcRegistryPlugin<?, ?> pluginP = new RpcRegistryPluginP(serviceManagerMap);
        return Arrays.asList(pluginC, pluginP);
    }

    private static Map<String, IServiceManager> getServiceManagerMap(String[] acceptableSystems, Function<String, IServiceManager> serviceManagerProvider) {
        Map<String, IServiceManager> map = new HashMap<>();
        if (null == acceptableSystems) {
            acceptableSystems = new String[]{null};
        }
        for (String system : acceptableSystems) {
            map.put(system, serviceManagerProvider.apply(system));
        }
        return map;
    }

    @Override
    public void onHandleSync(Input input, Output output) throws SyncException {
        IServiceManager manager = serviceManagerMap.get(input.getSystem());
        if (null == manager) {
            throw new SyncException("非注册系统，不允许同步");
        }
        try {
            onHandleSync(manager, input, output);
        } catch (RuntimeException e) {
            throw new SyncException(e.getMessage());
        }
    }

    protected abstract void onHandleSync(IServiceManager manager, Input in, Output out) throws SyncException;

}
