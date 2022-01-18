package com.soybeany.rpc.core.api;

import com.soybeany.rpc.core.exception.RpcPluginException;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public interface IRpcServiceProxy {

    /**
     * 创建指定接口的代理实例
     *
     * @param interfaceClass 指定的接口(类)
     * @return 代理对象
     */
    <T> T get(Class<T> interfaceClass) throws RpcPluginException;

}
