package com.soybeany.rpc.plugin;

import com.soybeany.rpc.model.ProviderParam;
import com.soybeany.rpc.model.ProviderResource;
import com.soybeany.rpc.model.ServerInfo;
import com.soybeany.rpc.model.ServerInfoProvider;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.util.*;

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
        switch (param.get(Constants.KEY_ACTION)) {
            case Constants.ACTION_GET_PROVIDERS:
                Map<String, ServerInfoProvider> map = new HashMap<>();
                for (String id : GSON.fromJson(param.get(Constants.KEY_SERVICE_ID_ARR), String[].class)) {
                    List<ServerInfo> infoList = new LinkedList<>();
                    Optional.ofNullable(providersMap.get(id))
                            .ifPresent(resources -> resources.forEach(r -> infoList.add(r.getInfo())));
                    map.put(id, new ServerInfoProvider(infoList));
                }
                result.put(Constants.KEY_PROVIDER_MAP, GSON.toJson(map));
                break;
            case Constants.ACTION_REGISTER_PROVIDERS:
                for (ProviderResource resource : GSON.fromJson(param.get(Constants.KEY_PROVIDER_MAP), ProviderParam.class).toResources()) {
                    providersMap.computeIfAbsent(resource.getId(), k -> new HashSet<>()).add(resource);
                }
                break;
            default:
                throw new RuntimeException("暂不支持此操作");
        }
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return Constants.TAG;
    }

    // ***********************内部方法****************************

}
