package com.soybeany.mq.consumer.anno;

import com.soybeany.mq.client.impl.MqImportSelectorImpl;
import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import com.soybeany.mq.consumer.impl.MqConsumerImportSelectorImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MqImportSelectorImpl.class, MqConsumerImportSelectorImpl.class})
public @interface EnableBdMqConsumer {

    /**
     * 消息拉取间隔
     */
    int pullIntervalSec();

    /**
     * 主题信息的存储仓库
     */
    Class<? extends ITopicInfoRepository> repository();

    /**
     * 出现异常时的处理器
     */
    Class<? extends IMqExceptionHandler> exceptionHandler();

    /**
     * 是否允许发送回执
     */
    boolean enableReceipt() default true;

}
