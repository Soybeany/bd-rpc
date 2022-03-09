package com.demo.service2;

import com.demo.service2.sync.RpcRegistrySyncerImpl;
import com.soybeany.mq.consumer.anno.EnableBdMqConsumer;
import com.soybeany.mq.consumer.impl.MqExceptionHandlerLogImpl;
import com.soybeany.mq.consumer.impl.TopicInfoRepositoryMemImpl;
import com.soybeany.rpc.unit.anno.EnableBdRpcUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBdMqConsumer(
        pullIntervalSec = 3,
        repository = TopicInfoRepositoryMemImpl.class,
        exceptionHandler = MqExceptionHandlerLogImpl.class
)
@EnableBdRpcUnit(syncer = RpcRegistrySyncerImpl.class)
@SpringBootApplication
class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
