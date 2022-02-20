package com.soybeany.rpc.client.api;

import com.soybeany.sync.client.api.ISystemConfig;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/12/17
 */
public interface IRpcClientService extends ISystemConfig {

    /**
     * 消费者时，为 “接口” 所在路径；<br/>
     * 生产者时，为 “实现类” 所在路径
     */
    void onSetupPkgPathToScan(Set<String> paths);

}
