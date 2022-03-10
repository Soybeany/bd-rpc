package com.soybeany.rpc.registry.impl;

import com.soybeany.rpc.registry.anno.EnableBdRpcRegistry;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class RpcRegistryImportSelectorImpl implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableBdRpcRegistry.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnableBdRpcRegistry");
        }
        return new String[]{
                ((Class<?>) map.get("syncer")).getName()
        };
    }

}
