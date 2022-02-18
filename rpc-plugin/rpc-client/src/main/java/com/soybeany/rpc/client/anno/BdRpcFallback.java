package com.soybeany.rpc.client.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识当前实现类为熔断后的实现
 *
 * @author Soybeany
 * @date 2021/10/31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BdRpcFallback {
}
