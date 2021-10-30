package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.ProviderParam;
import com.soybeany.rpc.core.model.ProviderResource;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.core.model.ServerInfoProvider;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

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

    // todo 带缓存，每10秒回调一次

    private final Map<String, Set<ProviderResource>> providersMap = new HashMap<>();

    @Override
    public synchronized void onHandleSync(Context ctx, Map<String, String> param, Map<String, String> result) {
        switch (param.get(KEY_ACTION)) {
            case ACTION_GET_PROVIDERS:
                Map<String, ServerInfoProvider> map = new HashMap<>();
                for (String id : GSON.fromJson(param.get(KEY_SERVICE_ID_ARR), String[].class)) {
                    List<ServerInfo> infoList = new LinkedList<>();
                    Optional.ofNullable(providersMap.get(id))
                            .ifPresent(resources -> resources.forEach(r -> infoList.add(r.getInfo())));
                    map.put(id, new ServerInfoProvider(infoList));
                }
                result.put(KEY_PROVIDER_MAP, GSON.toJson(map));
                break;
            case ACTION_REGISTER_PROVIDERS:
                for (ProviderResource resource : GSON.fromJson(param.get(KEY_PROVIDER_MAP), ProviderParam.class).toResources()) {
                    providersMap.computeIfAbsent(resource.getId(), k -> new HashSet<>()).add(resource);
                }
                break;
            default:
                throw new RuntimeException("暂不支持此操作");
        }
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
    }

    // ***********************内部方法****************************

}
