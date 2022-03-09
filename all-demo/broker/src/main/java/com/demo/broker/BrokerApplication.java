package com.demo.broker;

import com.demo.broker.impl.MqReceiptHandlerImpl;
import com.demo.broker.impl.MsgStorageManagerImpl;
import com.soybeany.mq.broker.anno.EnableBdMqBroker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBdMqBroker(
        msgStorageManager = MsgStorageManagerImpl.class,
        receiptHandler = MqReceiptHandlerImpl.class
)
@SpringBootApplication
class BrokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokerApplication.class, args);
    }

}
