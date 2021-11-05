package com.soybeany.rpc.demo.provider;


import com.soybeany.rpc.provider.ring.RingDataDAO;
import com.soybeany.rpc.provider.ring.RingDataProvider;
import org.junit.jupiter.api.Test;

/**
 * @author Soybeany
 * @date 2021/11/6
 */
class TestPluginTest {

    @Test
    void test() throws Exception {
        RingDataDAO.MemImpl<String> dao = new RingDataDAO.MemImpl<>();
        RingDataProvider<String> provider1 = new RingDataProvider.Builder<>(
                new AuthorizationProducer(), dao, 1000
        ).build();
        RingDataProvider<String> provider2 = new RingDataProvider.Builder<>(
                new AuthorizationProducer(), dao, 1000
        ).build();
        System.out.println(provider1.get());
        System.out.println(provider1.get());
        System.out.println(provider2.get());
        Thread.sleep(1000);
        System.out.println(provider1.get());
        System.out.println(provider2.get());
    }

}