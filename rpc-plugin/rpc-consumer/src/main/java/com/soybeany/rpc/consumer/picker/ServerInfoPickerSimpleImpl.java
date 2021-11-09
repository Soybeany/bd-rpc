package com.soybeany.rpc.consumer.picker;

import com.soybeany.rpc.core.model.ServerInfo;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public class ServerInfoPickerSimpleImpl implements ServerInfoPicker {

    private ServerInfo[] infoArr;
    private int nextIndex;

    @Override
    public synchronized void set(ServerInfo... infoArr) {
        this.infoArr = new ServerInfo[infoArr.length];
        System.arraycopy(infoArr, 0, this.infoArr, 0, infoArr.length);
        nextIndex = 0;
    }

    @Override
    public synchronized ServerInfo get() {
        if (null == infoArr || infoArr.length <= 0) {
            return null;
        }
        return infoArr[nextIndex++ % infoArr.length];
    }

}
