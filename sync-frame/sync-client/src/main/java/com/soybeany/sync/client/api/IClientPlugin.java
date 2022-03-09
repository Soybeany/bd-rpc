package com.soybeany.sync.client.api;

import com.soybeany.sync.client.model.SyncClientInfo;
import com.soybeany.sync.client.model.SyncState;
import com.soybeany.sync.core.api.IBasePlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public interface IClientPlugin<Input, Output> extends IBasePlugin<Input, Output> {

    /**
     * 启动回调前的回调，允许插件预处理
     *
     * @param appliedPlugins 同一service下应用的全部插件
     */
    default void onBeforeStartup(List<IClientPlugin<Object, Object>> appliedPlugins) {
    }

    /**
     * 应用启动时的回调
     *
     * @param info 同步客户端的信息
     */
    default void onStartup(SyncClientInfo info) {
    }

    /**
     * 启动回调后的回调，允许插件后处理
     *
     * @param appliedPlugins 同一service下应用的全部插件
     */
    default void onAfterStartup(List<IClientPlugin<Object, Object>> appliedPlugins) {
    }

    /**
     * 应用关闭时的回调
     */
    default void onShutdown() {
    }

    /**
     * 同步前的回调(需使用synchronized)
     *
     * @param uid    用于关联输入/输出的唯一标识
     * @param output 待传输至服务器的数据
     * @return 是否继续同步
     */
    default boolean onBeforeSync(String uid, Output output) throws Exception {
        return true;
    }

    /**
     * 同步结束的回调(必须先配置{@link #onSetupSyncTagToHandle}, 需使用synchronized)
     *
     * @param uid   用于关联输入/输出的唯一标识
     * @param input 入参
     */
    default void onAfterSync(String uid, Input input) throws Exception {
    }

    /**
     * 同步过程中出现异常时的回调(需使用synchronized)
     *
     * @param uid   用于关联输入/输出的唯一标识
     * @param state 所处的同步状态
     * @param e     异常
     */
    default void onSyncException(String uid, SyncState state, Exception e) throws Exception {
    }

}
