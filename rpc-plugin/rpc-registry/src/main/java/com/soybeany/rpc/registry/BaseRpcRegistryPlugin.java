package com.soybeany.rpc.registry;

import com.google.gson.Gson;
import com.soybeany.rpc.core.model.ProviderParam;
import com.soybeany.rpc.core.model.ProviderResource;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.core.model.ServerInfoProvider;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
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

    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        List<ServerInfo> list = new LinkedList<>();
        ServerInfo info = new ServerInfo();
        info.setAddress("sfer");
        list.add(info);
        String json = gson.toJson(list);
        System.out.println(json);
        String j2 = gson.toJson("sfwr");
        System.out.println(j2);

        Method m = BaseRpcRegistryPlugin.class.getDeclaredMethod("test", List.class, String.class);
        Type type = m.getGenericParameterTypes()[0];
        System.out.println(type);
        Type type2 = m.getGenericParameterTypes()[1];
        System.out.println(type2);
        List<ServerInfo> t = gson.fromJson(json, type);
        System.out.println(t.get(0).getAddress());
        System.out.println(gson.fromJson(j2, type2).toString());
    }

    private void test(List<ServerInfo> list, String w) {

    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
    }

    // ***********************内部方法****************************

}
