package com.soybeany.rpc.client.impl;

import com.soybeany.rpc.client.anno.EnableBdRpc;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class RpcClientImportSelectorImpl implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableBdRpc.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnableBdRpc");
        }
        return new String[]{
                ((Class<?>) map.get("syncer")).getName()
        };
    }

}
