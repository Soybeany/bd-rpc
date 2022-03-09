package com.demo.registry;

import com.soybeany.rpc.registry.anno.EnableBdRpcRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBdRpcRegistry(syncer = RegistrySyncerImpl.class)
@SpringBootApplication
class RegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class, args);
    }

}
