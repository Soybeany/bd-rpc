package com.soybeany.sync.core.api;

import com.soybeany.sync.core.exception.ISyncExceptionMsgProvider;
import com.soybeany.util.ExceptionUtils;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public interface IBasePlugin<Input, Output> extends Comparable<IBasePlugin<?, ?>> {

    static void checkPlugins(List<? extends IBasePlugin<Object, Object>> plugins) {
        Set<String> tags = new HashSet<>();
        for (IBasePlugin<Object, Object> plugin : plugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            if (!tags.add(tag)) {
                throw new RuntimeException("sync插件tag不能有重复值(" + tag + ")");
            }
        }
    }

    /**
     * 转换为安全的操作，用于定时器稳定运行
     */
    static Runnable toSafeRunnable(Runnable runnable, Logger log) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                if (e instanceof ISyncExceptionMsgProvider) {
                    log.warn(((ISyncExceptionMsgProvider) e).getMsg());
                } else {
                    log.error(ExceptionUtils.getExceptionDetail(e));
                }
            }
        };
    }

    @Override
    default int compareTo(IBasePlugin<?, ?> o) {
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
    String onSetupSyncTagToHandle();

    /**
     * 配置输入的类型
     */
    Class<Input> onGetInputClass();

    /**
     * 配置输出的类型
     */
    Class<Output> onGetOutputClass();

}
