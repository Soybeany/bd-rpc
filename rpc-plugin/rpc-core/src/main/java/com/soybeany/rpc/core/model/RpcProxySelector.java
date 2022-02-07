package com.soybeany.rpc.core.model;

import lombok.AllArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/18
 */
@AllArgsConstructor
public class RpcProxySelector<T> {

    private static final ThreadLocal<String> SERVICE_TAG = new ThreadLocal<>();
    private final T target;

    public static String getAndRemoveTag() {
        String tag = SERVICE_TAG.get();
        SERVICE_TAG.remove();
        return tag;
    }

    public T get(String tag) {
        SERVICE_TAG.set(tag);
        return target;
    }

}
