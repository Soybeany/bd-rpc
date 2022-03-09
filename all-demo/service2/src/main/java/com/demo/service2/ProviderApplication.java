package com.demo.service2;

import com.soybeany.mq.consumer.anno.EnableBdMqConsumer;
import com.soybeany.mq.consumer.impl.MqExceptionHandlerLogImpl;
import com.soybeany.mq.consumer.impl.TopicInfoRepositoryMemImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBdMqConsumer(
        pullIntervalSec = 3,
        repository = TopicInfoRepositoryMemImpl.class,
        exceptionHandler = MqExceptionHandlerLogImpl.class
)
@SpringBootApplication
class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
