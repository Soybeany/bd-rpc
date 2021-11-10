package com.soybeany.rpc.registry;

import com.google.gson.reflect.TypeToken;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * 客户端管理插件
 *
 * @author Soybeany
 * @date 2021/10/26
 */
@Component
public abstract class BaseRpcRegistryPlugin implements IServerPlugin {

    private static final Type TYPE_ID_SET = new TypeToken<Set<String>>() {
    }.getType();

    private final IServiceManager serviceManager = onSetupServiceManager();

    @Override
    public void onHandleSync(Context ctx, Map<String, String> param, Map<String, String> result) {
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

    // ***********************子类实现****************************

    protected abstract IServiceManager onSetupServiceManager();

}
