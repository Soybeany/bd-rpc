package com.soybeany.rpc.client.model;

import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.sync.core.model.SerializeType;
import com.soybeany.sync.core.util.SyncSerializeUtils;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;


/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Data
public class RpcMethodInfo {

    private final String serviceId;
    private final String methodName;
    private final String[] paramClazzNames;
    private final String[] argStrings;
    private final SerializeType returnType;
    private final SerializeType[] paramTypes;

    public RpcMethodInfo(String serviceId, Method method, TypeInfo typeInfo, Object... args) throws Exception {
        this.serviceId = serviceId;
        this.methodName = method.getName();
        this.paramClazzNames = toClassNames(method.getParameterTypes());
        this.returnType = Optional.ofNullable(typeInfo).map(info -> info.returnType).orElse(null);
        this.paramTypes = Optional.ofNullable(typeInfo).map(info -> info.paramTypes).orElse(null);
        this.argStrings = argsToString(method, args);
    }

    /**
     * 获取方法对象
     *
     * @param obj 含有此方法的对象
     */
    public Method getMethod(Object obj) {
        try {
            return obj.getClass().getMethod(methodName, toClass(paramClazzNames));
        } catch (Exception e) {
            throw new RpcPluginException("没有找到对应的方法:" + e.getMessage());
        }
    }

    public String getMethodDesc() {
        return methodName + "(" + Arrays.asList(paramClazzNames) + ")";
    }

    public Object[] getArgs(Method method) throws Exception {
        Type[] types = method.getGenericParameterTypes();
        Object[] args = new Object[argStrings.length];
        for (int i = 0; i < argStrings.length; i++) {
            args[i] = SyncSerializeUtils.fromString(paramTypes[i], types[i], argStrings[i]);
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

    private String[] argsToString(Method method, Object... objs) throws Exception {
        String[] jsons = new String[null != objs ? objs.length : 0];
        for (int i = 0; i < jsons.length; i++) {
            jsons[i] = SyncSerializeUtils.toString(paramTypes[i], objs[i],
                    e -> new RpcPluginException("方法“" + method + "”的入参中含有不可序列化的对象“" + e.getMessage() + "”")
            );
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

    // ***********************内部类****************************

    @Data
    public static class TypeInfo {
        private final SerializeType returnType;
        private final SerializeType[] paramTypes;
    }

}
