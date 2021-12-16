package com.soybeany.rpc.core.model;

import com.soybeany.rpc.core.anno.BdFallback;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.sync.core.api.IClientPlugin;
import org.springframework.lang.NonNull;

import java.util.Map;

import static com.soybeany.rpc.core.model.BdRpcConstants.KEY_SYSTEM;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public abstract class BaseRpcClientPlugin<T> implements IClientPlugin {

    @Override
    public void onSendSync(Map<String, String> result) {
        result.put(KEY_SYSTEM, onSetupSystem());
    }

    protected static String getId(BdRpc annotation) {
        return annotation.serviceId() + "-" + annotation.version();
    }

    // ***********************子类方法****************************

    protected boolean isFallbackImpl(Object obj) {
        return null != obj.getClass().getAnnotation(BdFallback.class);
    }

    // ***********************子类实现****************************

    public abstract T init();

    protected abstract String onSetupSystem();

    /**
     * 设置待扫描的路径
     *
     * @return 路径值(以该值开始)
     */
    @NonNull
    protected abstract String[] onSetupPkgPathToScan();

}
