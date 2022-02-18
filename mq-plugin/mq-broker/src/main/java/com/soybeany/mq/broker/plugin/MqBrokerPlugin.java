package com.soybeany.mq.broker.plugin;

import com.soybeany.mq.broker.api.IStorageManager;
import com.soybeany.sync.server.api.IServerPlugin;
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

    public static List<IServerPlugin<?, ?>> get(IStorageManager storageManager) {
        return Arrays.asList(
                new MqBrokerPluginC(storageManager),
                new MqBrokerPluginP(storageManager)
        );
    }

}
