package com.soybeany.rpc.consumer.impl;

import com.soybeany.rpc.consumer.anno.EnableBdRpcConsumer;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class RpcConsumerImportSelectorImpl implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableBdRpcConsumer.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnableBdRpcConsumer");
        }
        return new String[]{
                ((Class<?>) map.get("syncer")).getName()
        };
    }

}
