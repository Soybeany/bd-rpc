package com.demo.service1;

import com.demo.service1.sync.RpcRegistrySyncerImpl;
import com.soybeany.mq.producer.anno.EnableBdMqProducer;
import com.soybeany.rpc.unit.anno.EnableBdRpcUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBdMqProducer
@EnableBdRpcUnit(syncer = RpcRegistrySyncerImpl.class)
@SpringBootApplication
class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
