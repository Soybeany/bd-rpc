package com.soybeany.mq.consumer.impl;

import com.soybeany.mq.consumer.anno.EnableBdMqConsumer;
import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Slf4j
public class MqConsumerImportSelectorImpl implements ImportSelector {

    @SuppressWarnings("unchecked")
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableBdMqConsumer.class.getName());
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
        MqConsumerPluginProvider.setInfo(new MqConsumerPluginProvider.MqConsumerPluginInfo(
                (int) map.get("pullIntervalSec"), repository, exceptionHandler, (boolean) map.get("enableReceipt")
        ));
        return new String[]{MqConsumerPluginProvider.class.getName()};
    }

}
