package com.demo.service2;

import com.soybeany.mq.client.anno.EnableBdMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBdMq
@SpringBootApplication
class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
