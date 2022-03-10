package com.soybeany.rpc.provider.anno;

import com.soybeany.rpc.provider.BaseRpcProviderRegistrySyncerImpl;
import com.soybeany.rpc.provider.impl.RpcProviderImportSelectorImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcProviderImportSelectorImpl.class)
public @interface EnableBdRpcProvider {

    Class<? extends BaseRpcProviderRegistrySyncerImpl> syncer();

}
