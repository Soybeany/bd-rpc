package com.soybeany.rpc.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BdRpcCache {

    /**
     * 描述该缓存
     */
    String desc();

    /**
     * 是否需要日志输出
     */
    boolean needLog() default true;

    /**
     * 是否使用param的md5作为缓存key(能减少复杂入参的存储空间)，否则只使用json
     */
    boolean useMd5Key() default true;

    /**
     * 数据容量
     */
    int capacity() default 100;

    /**
     * 缓存的超时，默认永不超时（单位：毫秒）
     */
    int expiry() default Integer.MAX_VALUE;

    /**
     * 异常时的超时，默认1分钟（单位：毫秒）
     */
    int fastFailExpiry() default 60 * 1000;

}
