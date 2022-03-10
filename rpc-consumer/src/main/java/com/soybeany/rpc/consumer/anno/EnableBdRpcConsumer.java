package com.soybeany.rpc.consumer.anno;

import com.soybeany.rpc.consumer.BaseRpcConsumerRegistrySyncerImpl;
import com.soybeany.rpc.consumer.impl.RpcConsumerImportSelectorImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcConsumerImportSelectorImpl.class)
public @interface EnableBdRpcConsumer {

    Class<? extends BaseRpcConsumerRegistrySyncerImpl> syncer();

}
