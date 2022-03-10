package com.soybeany.rpc.client.anno;

import com.soybeany.rpc.client.impl.RpcClientImportSelectorImpl;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 若没有使用其它BdRpc的启用注解时，才需使用此注解
 *
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcClientImportSelectorImpl.class)
public @interface EnableBdRpc {

    Class<? extends BaseClientSyncerImpl> syncer();

}
