package com.soybeany.mq.client.impl;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class MqImportSelectorImpl implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{MqExApiPkgProviderImpl.class.getName()};
    }
}
