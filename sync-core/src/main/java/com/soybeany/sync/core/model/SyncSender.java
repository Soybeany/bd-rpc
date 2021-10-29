package com.soybeany.sync.core.model;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public interface SyncSender {

    void send(Map<String, String> data);

}
