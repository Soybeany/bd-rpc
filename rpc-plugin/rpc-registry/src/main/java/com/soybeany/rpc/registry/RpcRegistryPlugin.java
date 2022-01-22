package com.soybeany.rpc.registry;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.model.BaseClientOutput;
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
public abstract class RpcRegistryPlugin<Input extends BaseClientOutput, Output> implements IServerPlugin<Input, Output> {

    private final Map<String, IStorageManager> storageManagerMap;

    public static List<IServerPlugin<?, ?>> get(String[] acceptableSystems, Function<String, IStorageManager> storageManagerProvider) {
        Map<String, IStorageManager> storageManagerMap = getStorageManagerMap(acceptableSystems, storageManagerProvider);
        return Arrays.asList(
                new RpcRegistryPluginC(storageManagerMap),
                new RpcRegistryPluginP(storageManagerMap)
        );
    }

    private static Map<String, IStorageManager> getStorageManagerMap(String[] acceptableSystems, Function<String, IStorageManager> storageManagerProvider) {
        Map<String, IStorageManager> map = new HashMap<>();
        if (null == acceptableSystems) {
            acceptableSystems = new String[]{null};
        }
        for (String system : acceptableSystems) {
            map.put(system, storageManagerProvider.apply(system));
        }
        return map;
    }

    @Override
    public void onHandleSync(Input input, Output output) throws SyncException {
        IStorageManager manager = storageManagerMap.get(input.getSystem());
        if (null == manager) {
            throw new SyncException("非注册系统，不允许同步");
        }
        try {
            onHandleSync(manager, input, output);
        } catch (RuntimeException e) {
            throw new SyncException(e.getMessage());
        }
    }

    protected abstract void onHandleSync(IStorageManager manager, Input in, Output out) throws SyncException;

}
