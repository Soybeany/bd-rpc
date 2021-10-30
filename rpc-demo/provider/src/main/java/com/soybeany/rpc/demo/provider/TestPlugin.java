package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.provider.BaseRpcProviderPlugin;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class TestPlugin extends BaseRpcProviderPlugin {

    @Override
    protected String onSetupScanPkg() {
        return "com.soybeany";
    }

}
