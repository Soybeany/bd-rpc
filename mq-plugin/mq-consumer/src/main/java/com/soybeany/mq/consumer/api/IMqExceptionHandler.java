package com.soybeany.mq.consumer.api;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqExceptionHandler {

    /**
     * @return 是否允许更新最新同步值
     */
    boolean onException(Exception e, List<String> payloads, IMqMsgHandler handler);

}
