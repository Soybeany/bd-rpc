package com.soybeany.rpc.consumer.api;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public interface IRpcExApiPkgProvider {

    /**
     * “接口” 所在路径
     */
    void onSetupApiPkgToScan(Set<String> paths);

}
