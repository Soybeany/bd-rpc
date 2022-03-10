package com.soybeany.mq.producer.anno;

import com.soybeany.mq.client.impl.MqImportSelectorImpl;
import com.soybeany.mq.producer.impl.MqProducerPluginProvider;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MqImportSelectorImpl.class, MqProducerPluginProvider.class})
public @interface EnableBdMqProducer {
}
