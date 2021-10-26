package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.server.plugin.BaseHeartBeatPlugin;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@Component
public class HeartBeatPlugin extends BaseHeartBeatPlugin {
    @Override
    protected void onStorageHeartBeatInfo(String ip) {
        System.out.println("接收到ip为" + ip + "的心跳信号");
    }
}
