package com.soybeany.rpc.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识此方法支持批量
 * <br/>使用批量调用时，方法上的{@link BdRpcCache}(若同时使用)将失效
 *
 * @author Soybeany
 * @date 2022/2/7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BdRpcBatch {

    /**
     * 方法id(同一个类中唯一)
     */
    String methodId();

}
