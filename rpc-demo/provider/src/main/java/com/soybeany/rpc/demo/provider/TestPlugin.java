package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.provider.BaseRpcProviderPlugin;
import com.soybeany.sync.core.util.NetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class TestPlugin extends BaseRpcProviderPlugin {

    @Value("${server.port}")
    private Integer port;
    @Value("${server.servlet.context-path}")
    private String path;

    @Override
    protected String onSetupScanPkg() {
        return "com.soybeany";
    }

    @Override
    protected ServerInfo onGetServerInfo() {
        ServerInfo info = new ServerInfo();
        info.setAddress(NetUtils.getLocalIpAddress());
        info.setPort(port);
        info.setContext(path);
        info.setAuthorization("123456");
        return info;
    }
}
