package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqMsgAsyncSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqProducerImpl extends BaseClientServiceImpl implements IMqMsgSender {

    private MqProducerPlugin plugin;

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = new MqProducerPlugin());
    }

    @Override
    public void syncSend(MqProducerMsg msg) throws MqPluginException {
        plugin.syncSend(msg);
    }

    @Override
    public void asyncSend(MqProducerMsg msg, IMqMsgAsyncSendCallback callback) {
        plugin.asyncSend(msg, callback);
    }
}
