package com.soybeany.rpc.core.model;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.sync.core.api.IClientPlugin;
import org.springframework.lang.NonNull;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public abstract class BaseRpcClientPlugin<Input, Output> implements IClientPlugin<Input, Output> {

    protected static String getId(String version, BdRpc annotation) {
        return annotation.serviceId() + "-" + version;
    }

    // ***********************子类实现****************************

    public abstract void init();

    /**
     * 设置待扫描的路径
     *
     * @return 路径值(以该值开始)
     */
    @NonNull
    protected abstract String[] onSetupPkgPathToScan();

}
