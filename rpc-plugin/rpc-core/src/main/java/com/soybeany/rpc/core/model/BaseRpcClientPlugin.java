package com.soybeany.rpc.core.model;

import com.soybeany.rpc.core.anno.BdFuse;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.sync.core.api.IClientPlugin;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public abstract class BaseRpcClientPlugin implements IClientPlugin {

    protected static String getId(BdRpc annotation) {
        return annotation.serviceId() + "-" + annotation.version();
    }

    // ***********************子类方法****************************

    protected boolean isFuseImpl(Object obj) {
        return null != obj.getClass().getAnnotation(BdFuse.class);
    }

    // ***********************子类实现****************************

    /**
     * 设置扫描的路径
     *
     * @return 路径值(以该值开始)
     */
    protected abstract String onSetupScanPkg();

}
