package com.soybeany.rpc.core.anno;

import com.soybeany.sync.core.model.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识 “方法返回值/入参” 需要用哪种方式序列化<br/>
 * 不使用此注解时，默认使用{@link SerializeType#GSON}<br/>
 * 使用时，默认使用{@link SerializeType#JAVA}
 *
 * @author Soybeany
 * @date 2022/2/7
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BdRpcSerialize {

    /**
     * 指定序列化方式
     */
    SerializeType value() default SerializeType.JAVA;

}
