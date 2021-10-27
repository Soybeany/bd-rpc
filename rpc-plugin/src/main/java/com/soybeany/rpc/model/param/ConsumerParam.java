package com.soybeany.rpc.model.param;

import com.soybeany.rpc.model.resource.RpcInfo;
import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class ConsumerParam {

    /**
     * 调用信息
     */
    private RpcInfo info;

    /**
     * 接收变更通知的路径(为null则不监听变更)
     */
    private String notifyPath;

}
