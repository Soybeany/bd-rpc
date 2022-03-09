package com.soybeany.mq.client.anno;

import com.soybeany.mq.client.impl.MqImportSelectorImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 若没有使用其它BdMq的启用注解时，才需使用此注解
 *
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MqImportSelectorImpl.class)
public @interface EnableBdMq {
}
