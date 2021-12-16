package com.soybeany.rpc.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * todo 允许配置超时
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
    String serviceId();

    /**
     * 服务版本号
     */
    int version() default 0;

}
