package com.soybeany.rpc.plugin;

import com.soybeany.rpc.model.Constants;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.model.Context;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public abstract class BaseRpcConsumerPlugin extends BaseRpcClientPlugin {

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {

    }

    @Override
    public void onHandleSync(Context ctx, Map<String, String> param) {

    }

    @Override
    public String onSetupSyncTagToHandle() {
        return Constants.TAG;
    }

    // ***********************内部方法****************************

}
