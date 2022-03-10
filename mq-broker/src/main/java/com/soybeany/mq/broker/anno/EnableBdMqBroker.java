package com.soybeany.mq.broker.anno;

import com.soybeany.mq.broker.impl.MqBrokerImportSelectorImpl;
import com.soybeany.mq.core.api.IMqMsgStorageManager;
import com.soybeany.mq.core.api.IMqReceiptHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MqBrokerImportSelectorImpl.class)
public @interface EnableBdMqBroker {

    Class<? extends IMqMsgStorageManager> msgStorageManager();

    Class<? extends IMqReceiptHandler> receiptHandler();

}
