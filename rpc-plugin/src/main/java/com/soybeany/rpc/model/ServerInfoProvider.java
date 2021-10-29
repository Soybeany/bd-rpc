package com.soybeany.rpc.model;

import java.util.Collection;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public class ServerInfoProvider {

    private final ServerInfo[] infoArr;
    private int index = -1;

    public ServerInfoProvider(ServerInfo... infoArr) {
        this.infoArr = new ServerInfo[infoArr.length];
        System.arraycopy(infoArr, 0, this.infoArr, 0, infoArr.length);
    }

    public ServerInfoProvider(Collection<ServerInfo> infoList) {
        this.infoArr = new ServerInfo[infoList.size()];
        int index = 0;
        for (ServerInfo info : infoList) {
            this.infoArr[index++] = info;
        }
    }

    public ServerInfo get() {
        if (null == infoArr) {
            return null;
        }
        return infoArr[++index % infoArr.length];
    }

}
