package com.soybeany.rpc.plugin;

import com.soybeany.rpc.model.BdRpc;
import com.soybeany.rpc.utl.ReflectUtils;
import com.soybeany.sync.core.api.IClientPlugin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public abstract class BaseRpcClientPlugin implements IClientPlugin {

    @Autowired
    private ApplicationContext appContext;

    @PostConstruct
    private void onInit() {
        for (String name : appContext.getBeanDefinitionNames()) {
            Object bean = appContext.getBean(name);
            Optional.ofNullable(ReflectUtils.getAnnotation(onSetupScanPkg(), BdRpc.class, bean))
                    .ifPresent(bdRpc -> onHandleBean(bdRpc, bean));
        }
    }

    /**
     * 设置扫描的路径
     */
    protected abstract String onSetupScanPkg();


    protected abstract void onHandleBean(BdRpc bdRpc, Object bean);

}
