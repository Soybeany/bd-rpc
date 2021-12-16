package com.soybeany.sync.core.api;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public interface IClientPlugin extends IBasePlugin {

    /**
     * 应用启动时的回调
     */
    default void onStartup() {
    }

    /**
     * 触发心跳时的回调
     *
     * @param result 待传输至服务器的数据
     */
    void onSendSync(Map<String, String> result);

    /**
     * 处理同步的回调(必须先配置{@link #onSetupSyncTagToHandle})
     *
     * @param param 入参
     */
    default void onHandleSync(Map<String, String> param) {
    }

}
