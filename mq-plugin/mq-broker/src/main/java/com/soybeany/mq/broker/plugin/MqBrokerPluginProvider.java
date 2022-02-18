package com.soybeany.mq.broker.plugin;

import com.soybeany.mq.broker.api.IStorageManager;
import com.soybeany.sync.server.api.IServerPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public class MqBrokerPluginProvider {

    public static List<IServerPlugin<?, ?>> get(IStorageManager storageManager) {
        return Arrays.asList(
                new MqBrokerPluginC(storageManager),
                new MqBrokerPluginP(storageManager)
        );
    }

}
