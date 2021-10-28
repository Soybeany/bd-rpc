package com.soybeany.rpc.demo.server;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class Test2Plugin implements IServerPlugin {
    @Override
    public String onSetupSyncTagToHandle() {
        return "test2";
    }

    @Override
    public void onHandleSync(Context ctx, Map<String, String> param, Map<String, String> result) {
        System.out.println("收到心跳:" + param);
        result.put("b", "ok");
    }
}
