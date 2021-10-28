package com.soybeany.rpc.utl;

import java.lang.annotation.Annotation;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
public class ReflectUtils {

    /**
     * 从一个指定对象中获取指定的注解
     */
    public static <T extends Annotation> T getAnnotation(String pkgStartWith, Class<T> annotationClass, Object obj) {
        Class<?> clazz = obj.getClass();
        String pkgName = clazz.getPackage().getName();
        if (!pkgName.startsWith(pkgStartWith)) {
            return null;
        }
        T annotation = null;
        while (!Object.class.equals(clazz)) {
            annotation = clazz.getAnnotation(annotationClass);
            if (null != annotation) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        return annotation;
    }

}
