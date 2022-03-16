package com.soybeany.mq.producer.impl;

import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.producer.api.IMqMsgSender;
import com.soybeany.mq.producer.plugin.MqProducerPlugin;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.api.IExClientPluginProvider;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import java.io.Serializable;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class MqProducerPluginProvider implements IExClientPluginProvider, IMqMsgSender, ImportAware {

    private MqProducerPlugin plugin;

    @Override
    public <T extends Serializable> void send(String topic, MqProducerMsg<T> msg) {
        plugin.send(topic, msg);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        // 若支持多syncer，需在注解中添加支持的syncerId数组，然后设置插件时按需添加
    }

    @Override
    public void onSetupPlugins(String syncerId, List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = new MqProducerPlugin());
    }

}
