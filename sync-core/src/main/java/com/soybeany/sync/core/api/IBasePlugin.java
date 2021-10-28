package com.soybeany.sync.core.api;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public interface IBasePlugin extends Comparable<IBasePlugin> {

    @Override
    default int compareTo(IBasePlugin o) {
        return o.priority() - priority();
    }

    /**
     * 优先级，值越大则越先被执行
     *
     * @return 具体值
     */
    default int priority() {
        return 0;
    }

    /**
     * 配置支持处理的同步标签
     *
     * @return 标签值
     */
    default String onSetupSyncTagToHandle() {
        return null;
    }

}
