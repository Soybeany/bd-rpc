package com.soybeany.rpc.core;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public interface INormServicePlugin extends IServicePlugin {

    /**
     * 配置支持处理的标签
     *
     * @return 标签值
     */
    String onSetupHandleTag();

}
