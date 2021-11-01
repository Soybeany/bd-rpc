package com.soybeany.rpc.core.utl;

import com.soybeany.rpc.core.model.MethodInfo;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface ServiceInvoker {

    Object invoke(MethodInfo info) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

}
