package com.soybeany.rpc.plugin;

import com.soybeany.rpc.model.BdRpc;
import com.soybeany.rpc.utl.ReflectUtils;
import com.soybeany.sync.core.api.IClientPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public abstract class BaseRpcClientPlugin implements IClientPlugin {

    protected static String getId(BdRpc annotation) {
        return annotation.serviceId() + "-" + annotation.version();
    }

    // ***********************子类实现****************************

    /**
     * 设置扫描的路径
     *
     * @return 路径值(以该值开始)
     */
    protected abstract String onSetupScanPkg();

}
