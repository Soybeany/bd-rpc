package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.provider.BaseRpcProviderPlugin;
import com.soybeany.sync.core.util.NetUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Log
@Component
public class TestPlugin extends BaseRpcProviderPlugin implements ApplicationListener<WebServerInitializedEvent> {

    @Value("${server.servlet.context-path}")
    private String path;

    private final String address = NetUtils.getLocalIpAddress();
    private Integer port = -1;

    @Override
    protected String onSetupScanPkg() {
        return "com.soybeany";
    }

    @Override
    protected ServerInfo onGetServerInfo() {
        log.info("发送");
        ServerInfo info = new ServerInfo();
        info.setAddress(address);
        info.setPort(port);
        info.setContext(path);
        return info;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        port = event.getWebServer().getPort();
    }
}
