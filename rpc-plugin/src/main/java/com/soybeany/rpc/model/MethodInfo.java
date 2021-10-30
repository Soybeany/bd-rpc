package com.soybeany.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Data
@AllArgsConstructor
public class MethodInfo {

    private String serviceId;
    private String methodName;
    private String[] clazzNames;
    private String[] argJsons;

    public static String[] toJsons(Object[] objs) {
        String[] jsons = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            jsons[i] = GSON.toJson(objs[i]);
        }
        return jsons;
    }

    public Object[] getArgs() throws ClassNotFoundException {
        Object[] result = new Object[argJsons.length];
        for (int i = 0; i < argJsons.length; i++) {
            result[i] = GSON.fromJson(argJsons[i], Class.forName(clazzNames[i]));
        }
        return result;
    }
}
