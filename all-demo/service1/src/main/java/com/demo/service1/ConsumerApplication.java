package com.demo.service1;

import com.soybeany.mq.producer.anno.EnableBdMqProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBdMqProducer
@SpringBootApplication
class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
