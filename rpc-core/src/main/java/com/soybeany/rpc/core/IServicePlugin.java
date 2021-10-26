package com.soybeany.rpc.core;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public interface IServicePlugin extends Comparable<IServicePlugin> {

    String SEPARATOR = "-";

    @Override
    default int compareTo(IServicePlugin o) {
        return o.priority() - priority();
    }

    /**
     * 优先级，值越大则越先被执行
     *
     * @return 具体值
     */
    default int priority() {
        return 0;
    }

    /**
     * 处理具体业务的回调
     *
     * @param request 请求
     * @param param   入参
     * @param result  数据
     */
    void onHandle(HttpServletRequest request, Map<String, String[]> param, Map<String, String> result);

}
