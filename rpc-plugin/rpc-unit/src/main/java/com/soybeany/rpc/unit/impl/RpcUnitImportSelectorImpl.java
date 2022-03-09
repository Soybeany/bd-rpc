package com.soybeany.rpc.unit.impl;

import com.soybeany.rpc.unit.anno.EnableBdRpcUnit;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class RpcUnitImportSelectorImpl implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableBdRpcUnit.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnableBdRpcUnit");
        }
        return new String[]{
                ((Class<?>) map.get("syncer")).getName()
        };
    }

}
