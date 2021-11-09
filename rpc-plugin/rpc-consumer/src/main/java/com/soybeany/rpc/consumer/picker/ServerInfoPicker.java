package com.soybeany.rpc.consumer.picker;

import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ServerInfo;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public class ServerInfoPicker {

    private final ServerInfo[] infoArr;
    private transient int index = -1;

    public ServerInfoPicker(ServerInfo... infoArr) {
        this.infoArr = new ServerInfo[infoArr.length];
        System.arraycopy(infoArr, 0, this.infoArr, 0, infoArr.length);
    }

    public ServerInfo get() {
        if (null == infoArr || infoArr.length <= 0) {
            throw new RpcPluginException("此id的服务提供者暂未注册");
        }
        return infoArr[++index % infoArr.length];
    }

}
