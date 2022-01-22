package com.soybeany.mq.registry;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IMqStorageManager {

    Set<String> load(String system);

    void save(String system, String syncUrl);

}
