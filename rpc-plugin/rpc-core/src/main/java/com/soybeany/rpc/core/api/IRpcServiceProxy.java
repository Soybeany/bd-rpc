package com.soybeany.rpc.core.api;

import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcBatchConfig;
import com.soybeany.rpc.core.model.RpcBatchResult;
import com.soybeany.rpc.core.model.RpcProxySelector;
import com.soybeany.rpc.core.model.RpcServerInfo;

import java.util.Map;

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
    <T> RpcProxySelector<T> getSelector(Class<T> interfaceClass) throws RpcPluginException;

    /**
     * 批量执行
     */
    default <T> Map<RpcServerInfo, RpcBatchResult<T>> batchInvoke(Class<T> interfaceClass, String methodId, Object... args) {
        return batchInvoke(interfaceClass, methodId, null, args);
    }

    /**
     * 批量执行(指定标签)
     */
    <T> Map<RpcServerInfo, RpcBatchResult<T>> batchInvoke(Class<T> interfaceClass, String methodId, RpcBatchConfig config, Object... args);

}
