package com.soybeany.rpc.registry.anno;

import com.soybeany.rpc.registry.BaseRpcRegistrySyncerImpl;
import com.soybeany.rpc.registry.impl.RpcRegistryImportSelectorImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcRegistryImportSelectorImpl.class)
public @interface EnableBdRpcRegistry {

    Class<? extends BaseRpcRegistrySyncerImpl> syncer();

}
