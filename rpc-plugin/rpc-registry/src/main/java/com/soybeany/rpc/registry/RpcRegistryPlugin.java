package com.soybeany.rpc.registry;

import com.google.gson.reflect.TypeToken;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.exception.SyncException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * 客户端管理插件
 *
 * @author Soybeany
 * @date 2021/10/26
 */
public class RpcRegistryPlugin implements IServerPlugin {

    private static final Type TYPE_ID_SET = new TypeToken<Set<String>>() {
    }.getType();

    private final Map<String, IServiceManager> serviceManagerMap = new HashMap<>();

    public RpcRegistryPlugin(String[] acceptableSystems, Function<String, IServiceManager> serviceManagerProvider) {
        for (String system : acceptableSystems) {
            serviceManagerMap.put(system, serviceManagerProvider.apply(system));
        }
    }

    @Override
    public void onHandleSync(Map<String, String> param, Map<String, String> result) throws SyncException {
        IServiceManager serviceManager = serviceManagerMap.get(param.get(KEY_SYSTEM));
        if (null == serviceManager) {
            throw new SyncException("非注册系统，不允许同步");
        }
        switch (param.get(KEY_ACTION)) {
            case ACTION_GET_PROVIDERS:
                Map<String, Set<ServerInfo>> map = new HashMap<>();
                for (String id : GSON.fromJson(param.get(KEY_SERVICE_ID_ARR), String[].class)) {
                    map.put(id, serviceManager.load(id));
                }
                result.put(KEY_PROVIDER_MAP, GSON.toJson(map));
                break;
            case ACTION_REGISTER_PROVIDERS:
                ServerInfo info = GSON.fromJson(param.get(KEY_PROVIDER_INFO), ServerInfo.class);
                Set<String> serviceIds = GSON.fromJson(param.get(KEY_SERVICE_ID_ARR), TYPE_ID_SET);
                serviceManager.save(info, serviceIds);
                break;
            default:
                throw new RpcPluginException("暂不支持此操作");
        }
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
    }

}
