package com.soybeany.rpc.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识此方法的返回值需要用哪种方式序列化
 *
 * @author Soybeany
 * @date 2022/2/7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BdRpcSerialize {

    enum Type {
        /**
         * gson方式
         */
        GSON,
        /**
         * java方式，即Serializable
         */
        JAVA
    }

    /**
     * 指定序列化方式
     */
    Type value() default Type.JAVA;

}
