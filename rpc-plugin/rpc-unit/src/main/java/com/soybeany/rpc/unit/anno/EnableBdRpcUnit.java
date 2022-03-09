package com.soybeany.rpc.unit.anno;

import com.soybeany.rpc.unit.BaseRpcUnitRegistrySyncerImpl;
import com.soybeany.rpc.unit.impl.RpcUnitImportSelectorImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcUnitImportSelectorImpl.class)
public @interface EnableBdRpcUnit {

    Class<? extends BaseRpcUnitRegistrySyncerImpl> syncer();

}
