package com.soybeany.rpc.provider.api;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public interface IRpcExImplPkgProvider {

    /**
     * “实现类” 所在路径
     */
    void onSetupImplPkgToScan(Set<String> paths);

}
