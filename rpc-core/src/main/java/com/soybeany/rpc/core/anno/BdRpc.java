package com.soybeany.rpc.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Soybeany
 * @date 2021/10/29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BdRpc {

    /**
     * 服务id(唯一)
     */
    String serviceId() default "";

    /**
     * 指定调用超时，若值小于0则使用全局配置(单位：秒)
     */
    int timeoutInSec() default -1;

}
