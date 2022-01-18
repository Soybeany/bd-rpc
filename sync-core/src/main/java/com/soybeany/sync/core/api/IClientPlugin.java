package com.soybeany.sync.core.api;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public interface IClientPlugin<Input, Output> extends IBasePlugin<Input, Output> {

    /**
     * 应用启动时的回调
     */
    default void onStartup() {
    }

    /**
     * 处理数据输出（触发心跳时的回调）
     *
     * @param output 待传输至服务器的数据
     */
    default void onHandleOutput(Output output) {
    }

    /**
     * 处理数据输入(必须先配置{@link #onSetupSyncTagToHandle})
     *
     * @param input 入参
     */
    default void onHandleInput(Input input) {
    }

}
