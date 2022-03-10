package com.soybeany.mq.broker.impl;

import com.soybeany.mq.broker.anno.EnableBdMqBroker;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class MqBrokerImportSelectorImpl implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableBdMqBroker.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnableBdMqBroker");
        }
        return new String[]{
                ((Class<?>) map.get("msgStorageManager")).getName(),
                ((Class<?>) map.get("receiptHandler")).getName()
        };
    }

}
