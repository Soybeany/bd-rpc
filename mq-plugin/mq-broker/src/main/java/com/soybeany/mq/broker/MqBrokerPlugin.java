package com.soybeany.mq.broker;

import com.soybeany.sync.core.api.IServerPlugin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MqBrokerPlugin<Input, Output> implements IServerPlugin<Input, Output> {

    public static List<IServerPlugin<?, ?>> get(IMessageManager messageManager) {
        return Arrays.asList(
                new MqBrokerPluginC(messageManager),
                new MqBrokerPluginP(messageManager)
        );
    }

}
