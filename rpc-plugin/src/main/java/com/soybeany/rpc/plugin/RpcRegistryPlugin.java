package com.soybeany.rpc.plugin;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 客户端管理插件
 *
 * @author Soybeany
 * @date 2021/10/26
 */
@Component
public class RpcRegistryPlugin implements IServerPlugin {

    // todo 带缓存，每10秒回调一次

    @Override
    public void onHandleSync(Context ctx, Map<String, String[]> param, Map<String, String> result) {

    }

}
