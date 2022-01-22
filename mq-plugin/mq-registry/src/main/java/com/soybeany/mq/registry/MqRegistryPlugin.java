package com.soybeany.mq.registry;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.BaseClientOutput;

import java.util.Arrays;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public abstract class MqRegistryPlugin<Input extends BaseClientOutput, Output> implements IServerPlugin<Input, Output> {

    public static List<IServerPlugin<?, ?>> get(IMqStorageManager storageManager) {
        return Arrays.asList(
                new MqRegistryPluginC(storageManager),
                new MqRegistryPluginB(storageManager)
        );
    }

}
