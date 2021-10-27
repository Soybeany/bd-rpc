package com.soybeany.sync.core.api;


import com.soybeany.sync.core.model.Context;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public interface IServerPlugin extends Comparable<IServerPlugin> {

    @Override
    default int compareTo(IServerPlugin o) {
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
     * 配置支持处理的同步标签
     *
     * @return 标签值
     */
    default String onSetupSyncTagToHandle() {
        return null;
    }

    /**
     * 处理同步的回调(必须先配置{@link #onSetupSyncTagToHandle})
     *
     * @param ctx    上下文
     * @param param  入参
     * @param result 数据
     */
    void onHandleSync(Context ctx, Map<String, String[]> param, Map<String, String> result);

}
