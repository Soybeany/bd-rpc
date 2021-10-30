package com.soybeany.rpc.core.model;

import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.soybeany.sync.core.util.RequestUtils.GSON;


/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Data
public class MethodInfo {

    private String serviceId;
    private String methodName;
    private String[] paramClazzNames;
    private String[] argJsons;

    public MethodInfo(String serviceId, Method method, Object... args) {
        this.serviceId = serviceId;
        this.methodName = method.getName();
        this.paramClazzNames = toClassNames(method.getParameterTypes());
        this.argJsons = toJsons(args);
    }

    /**
     * 获取方法对象
     *
     * @param obj 含有此方法的对象
     */
    public Method getMethod(Object obj) throws ClassNotFoundException, NoSuchMethodException {
        return obj.getClass().getMethod(methodName, toClass(paramClazzNames));
    }

    public Object[] getArgs(Method method) {
        Type[] types = method.getGenericParameterTypes();
        Object[] args = new Object[argJsons.length];
        for (int i = 0; i < argJsons.length; i++) {
            args[i] = GSON.fromJson(argJsons[i], types[i]);
        }
        return args;
    }

    // ***********************内部方法****************************

    private Class<?>[] toClass(String[] classNames) throws ClassNotFoundException {
        Class<?>[] classes = new Class[classNames.length];
        for (int i = 0; i < classNames.length; i++) {
            classes[i] = Class.forName(classNames[i]);
        }
        return classes;
    }

    private String[] toJsons(Object... objs) {
        String[] jsons = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            jsons[i] = GSON.toJson(objs[i]);
        }
        return jsons;
    }

    private String[] toClassNames(Class<?>[] classes) {
        String[] result = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            result[i] = classes[i].getName();
        }
        return result;
    }

}
