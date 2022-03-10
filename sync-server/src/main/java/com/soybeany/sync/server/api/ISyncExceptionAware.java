package com.soybeany.sync.server.api;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/2/19
 */
public interface ISyncExceptionAware {

    /**
     * 同步过程中出现异常时的回调
     *
     * @param plugins 受影响/源发的插件
     * @param e       异常
     */
    void onSyncException(List<IServerPlugin<Object, Object>> plugins, Exception e) throws Exception;

}
