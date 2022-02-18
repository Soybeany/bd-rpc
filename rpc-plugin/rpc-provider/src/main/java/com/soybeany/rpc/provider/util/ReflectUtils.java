package com.soybeany.rpc.provider.util;

import java.lang.annotation.Annotation;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
public class ReflectUtils {

    /**
     * 从一个指定对象中获取指定的注解
     */
    public static <T extends Annotation> T getAnnotation(String pkgStartWith, Class<T> annotationClass, Class<?> clazz) {
        Package pkg = clazz.getPackage();
        if (null == pkg) {
            return null;
        }
        if (null != pkgStartWith && !pkg.getName().startsWith(pkgStartWith)) {
            return null;
        }
        T annotation = null;
        while (!Object.class.equals(clazz)) {
            annotation = clazz.getAnnotation(annotationClass);
            if (null != annotation) {
                break;
            }
            annotation = getAnnotationFromInterfaces(annotationClass, clazz);
            if (null != annotation) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        return annotation;
    }

    private static <T extends Annotation> T getAnnotationFromInterfaces(Class<T> annotationClass, Class<?> objClass) {
        Class<?>[] classes = objClass.getInterfaces();
        if (classes.length == 0) {
            return null;
        }
        for (Class<?> clazz : classes) {
            T annotation = clazz.getAnnotation(annotationClass);
            if (null != annotation) {
                return annotation;
            }
            annotation = getAnnotationFromInterfaces(annotationClass, clazz);
            if (null != annotation) {
                return annotation;
            }
        }
        return null;
    }

}
