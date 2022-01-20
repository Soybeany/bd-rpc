package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqMsgHandler;
import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqConsumerImpl extends BaseClientServiceImpl {

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(new MqConsumerPlugin(onSetupMsgHandlers(), onSetupExceptionHandler()));
    }

    // ***********************子类实现****************************

    protected abstract List<IMqMsgHandler> onSetupMsgHandlers();

    protected abstract IMqExceptionHandler onSetupExceptionHandler();
}
