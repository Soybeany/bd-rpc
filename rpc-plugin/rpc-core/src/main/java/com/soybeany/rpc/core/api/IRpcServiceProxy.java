package com.soybeany.rpc.core.api;

import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcProxySelector;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public interface IRpcServiceProxy {

    /**
     * 创建指定接口的代理实例，默认支持全部标签，若需指定标签，使用{@link #getSelector}
     *
     * @param interfaceClass 指定的接口(类)
     * @return 代理对象
     */
    <T> T get(Class<T> interfaceClass) throws RpcPluginException;

    /**
     * 创建一个能动态指定标签的选择器，因为是基于{@link ThreadLocal}的实现，
     * 所以{@link RpcProxySelector#get}的返回值只能作为局部变量，否则可能会出现不可预知的问题
     */
    default <T> RpcProxySelector<T> getSelector(Class<T> interfaceClass) throws RpcPluginException {
        return new RpcProxySelector<>(get(interfaceClass));
    }

    /**
     * 批量执行
     */
    <T> IRpcBatchInvoker<T> getBatch(Class<?> interfaceClass, String methodId);

    /**
     * 批量执行(指定标签)
     */
    default <T> RpcProxySelector<IRpcBatchInvoker<T>> getBatchSelector(Class<?> interfaceClass, String methodId) {
        return new RpcProxySelector<>(getBatch(interfaceClass, methodId));
    }

}
