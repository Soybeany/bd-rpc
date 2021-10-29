package com.soybeany.sync.core.api;

import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.model.SyncSender;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public interface IClientPlugin extends IBasePlugin {

    /**
     * 应用启动时的回调
     *
     * @param sender 同步信号的发送者
     */
    default void onStartup(SyncSender sender) {
    }

    /**
     * 触发心跳时的回调
     *
     * @param ctx    上下文
     * @param result 待传输至服务器的数据
     */
    void onSendSync(Context ctx, Map<String, String> result);

    /**
     * 处理同步的回调(必须先配置{@link #onSetupSyncTagToHandle})
     *
     * @param ctx   上下文
     * @param param 入参
     */
    void onHandleSync(Context ctx, Map<String, String> param);

}
