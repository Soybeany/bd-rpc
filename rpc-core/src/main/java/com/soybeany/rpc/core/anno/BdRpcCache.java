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
    String desc() default "";

    /**
     * 是否需要日志输出
     */
    boolean needLog() default true;

    /**
     * 是否使用param的md5作为缓存key(能减少复杂入参的存储空间)，否则只使用json
     */
    boolean useMd5Key() default true;

    /**
     * 是否允许在数据源出现异常时，使用上一次已失效的缓存数据，使用异常的生存时间
     */
    boolean enableRenewExpiredCache() default false;

    /**
     * 存储id，若有设置，相同的storageId将共用缓存存储
     */
    String storageId() default "";

    /**
     * 数据容量
     */
    int capacity() default 100;

    /**
     * 正常数据的生存时间，用于一般场景，默认永不超时（单位：秒）
     */
    int ttl() default Integer.MAX_VALUE / 1000 - 1;

    /**
     * 异常的生存时间，用于防缓存穿透等场景，默认1分钟（单位：秒）
     */
    int ttlErr() default 60;

}
