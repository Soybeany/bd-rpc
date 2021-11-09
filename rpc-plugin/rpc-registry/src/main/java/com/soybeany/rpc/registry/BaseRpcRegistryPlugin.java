package com.soybeany.rpc.registry;

import com.google.gson.reflect.TypeToken;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;

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

    private final IResourceManager resourceManager = onSetupResourceManager();

    @Override
    public void onHandleSync(Context ctx, Map<String, String> param, Map<String, String> result) {
        switch (param.get(KEY_ACTION)) {
            case ACTION_GET_PROVIDERS:
                Map<String, List<ServerInfo>> map = new HashMap<>();
                for (String id : GSON.fromJson(param.get(KEY_SERVICE_ID_ARR), String[].class)) {
                    List<ServerInfo> infoList = new LinkedList<>();
                    Optional.ofNullable(resourceManager.load(id))
                            .ifPresent(resources -> resources.forEach(r -> infoList.add(r.getInfo())));
                    map.put(id, infoList);
                }
                result.put(KEY_PROVIDER_MAP, GSON.toJson(map));
                break;
            case ACTION_REGISTER_PROVIDERS:
                ServerInfo info = GSON.fromJson(param.get(KEY_PROVIDER_INFO), ServerInfo.class);
                Set<String> serviceIds = GSON.fromJson(param.get(KEY_SERVICE_ID_ARR), TYPE_ID_SET);
                toResources(info, serviceIds).forEach(resourceManager::save);
                break;
            default:
                throw new RpcPluginException("暂不支持此操作");
        }
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
    }

    // ***********************内部方法****************************

    private Set<ProviderResource> toResources(ServerInfo info, Collection<String> serviceIds) {
        Set<ProviderResource> set = new HashSet<>();
        Date date = new Date();
        serviceIds.forEach(id -> set.add(ProviderResource.getNew(id, info, date)));
        return set;
    }

    // ***********************子类实现****************************

    protected abstract IResourceManager onSetupResourceManager();

}
