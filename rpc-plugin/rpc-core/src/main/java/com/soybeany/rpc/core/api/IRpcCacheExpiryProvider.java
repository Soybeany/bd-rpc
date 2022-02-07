package com.soybeany.rpc.core.api;

/**
 * 用于设置了@BdCache的方法返回值对象上，能自定义修改缓存失效时间
 *
 * @author Soybeany
 * @date 2022/1/28
 */
public interface IRpcCacheExpiryProvider {

    int onSetupCacheExpiry();

}
