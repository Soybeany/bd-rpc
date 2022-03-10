package com.soybeany.mq.consumer.impl;

import com.soybeany.mq.consumer.anno.EnableBdMqConsumer;
import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.IMqMsgHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import com.soybeany.mq.consumer.plugin.MqConsumerPlugin;
import com.soybeany.rpc.client.api.IRpcOtherPluginsProvider;
import com.soybeany.sync.client.api.IClientPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Slf4j
public class MqConsumerPluginProvider implements IRpcOtherPluginsProvider, ImportAware {

    @Autowired
    private List<IMqMsgHandler<?>> handlers;

    private MqConsumerPluginInfo info;

    @SuppressWarnings("unchecked")
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableBdMqConsumer.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnableBdMqConsumer");
        }
        ITopicInfoRepository repository;
        IMqExceptionHandler exceptionHandler;
        try {
            repository = ((Class<ITopicInfoRepository>) map.get("repository"))
                    .getConstructor()
                    .newInstance();
            exceptionHandler = ((Class<IMqExceptionHandler>) map.get("exceptionHandler"))
                    .getConstructor()
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException("EnableBdMqConsumer中实例化异常：" + e.getMessage());
        }
        info = new MqConsumerPluginInfo((int) map.get("pullIntervalSec"), repository, exceptionHandler, (boolean) map.get("enableReceipt"));
    }

    @Override
    public List<IClientPlugin<?, ?>> onSetupPlugins(String syncerId) {
        return Collections.singletonList(
                new MqConsumerPlugin(info.pullIntervalSec, handlers, info.repository, info.exceptionHandler, info.enableReceipt)
        );
    }

    // ***********************内部类****************************

    @RequiredArgsConstructor
    private static class MqConsumerPluginInfo {
        private final int pullIntervalSec;
        private final ITopicInfoRepository repository;
        private final IMqExceptionHandler exceptionHandler;
        private final boolean enableReceipt;
    }

}
