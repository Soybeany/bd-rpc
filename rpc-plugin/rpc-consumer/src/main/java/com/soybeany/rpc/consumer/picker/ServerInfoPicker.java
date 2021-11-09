package com.soybeany.rpc.consumer.picker;

import com.soybeany.rpc.core.model.ServerInfo;

/**
 * @author Soybeany
 * @date 2021/11/9
 */
public interface ServerInfoPicker {

    void set(ServerInfo... infoArr);

    ServerInfo get();

    default void onRequestFailure(ServerInfo info) {
    }

}
