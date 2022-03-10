package com.soybeany.rpc.provider.impl;

import com.soybeany.rpc.provider.anno.EnableBdRpcProvider;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class RpcProviderImportSelectorImpl implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableBdRpcProvider.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnableBdRpcProvider");
        }
        return new String[]{
                ((Class<?>) map.get("syncer")).getName()
        };
    }

}
