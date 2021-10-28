package com.soybeany.rpc.demo.provider;

import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class TestPlugin implements IClientPlugin {
    @Override
    public String onSetupSyncTagToHandle() {
        return "test";
    }

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {
        result.put("what", "a");
        System.out.println("准备发送心跳");
    }

    @Override
    public void onHandleSync(Context ctx, Map<String, String> param) {
        System.out.println("处理心跳回执:" + param);
    }
}
