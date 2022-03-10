package com.soybeany.rpc.provider.util;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
public class ReflectUtils {

    /**
     * 从一个指定对象中获取指定的注解
     */
    public static <T extends Annotation> Result<T> getAnnotation(String pkgStartWith, Class<T> annotationClass, Class<?> clazz) {
        Package pkg = clazz.getPackage();
        if (null == pkg) {
            return null;
        }
        if (null != pkgStartWith && !pkg.getName().startsWith(pkgStartWith)) {
            return null;
        }
        Result<T> result = null;
        while (!Object.class.equals(clazz)) {
            T annotation = clazz.getAnnotation(annotationClass);
            if (null != annotation) {
                result = new Result<>(annotation, clazz);
                break;
            }
            result = getAnnotationFromInterfaces(annotationClass, clazz);
            if (null != result) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    private static <T extends Annotation> Result<T> getAnnotationFromInterfaces(Class<T> annotationClass, Class<?> objClass) {
        Class<?>[] classes = objClass.getInterfaces();
        if (classes.length == 0) {
            return null;
        }
        for (Class<?> clazz : classes) {
            T annotation = clazz.getAnnotation(annotationClass);
            if (null != annotation) {
                return new Result<>(annotation, clazz);
            }
            Result<T> result = getAnnotationFromInterfaces(annotationClass, clazz);
            if (null != result) {
                return result;
            }
        }
        return null;
    }

    // ***********************内部类****************************

    @Data
    public static class Result<T extends Annotation> {
        private final T annotation;
        private final Class<?> clazz;
    }

}
