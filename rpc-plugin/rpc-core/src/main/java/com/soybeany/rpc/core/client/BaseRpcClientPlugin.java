package com.soybeany.rpc.core.client;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.sync.core.api.IClientPlugin;
import org.springframework.lang.NonNull;

import java.util.*;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public abstract class BaseRpcClientPlugin<Input, Output> implements IClientPlugin<Input, Output> {

    protected static final List<String> FRAME_PKG_PATHS = Collections.singletonList("com.soybeany");

    private List<String> postTreatPkgPathsToScan;

    protected static String getId(String version, BdRpc annotation) {
        return annotation.serviceId() + "-" + version;
    }

    protected List<String> getPostTreatPkgPathsToScan() {
        if (null == postTreatPkgPathsToScan) {
            List<String> paths = new ArrayList<>(onSetupPkgPathToScan());
            paths.addAll(FRAME_PKG_PATHS);
            paths.sort(Comparator.comparingInt(String::length));
            postTreatPkgPathsToScan = new ArrayList<>();
            out:
            for (String path : paths) {
                for (String p : postTreatPkgPathsToScan) {
                    if (path.startsWith(p)) {
                        continue out;
                    }
                }
                postTreatPkgPathsToScan.add(path);
            }
        }
        return postTreatPkgPathsToScan;
    }

    // ***********************子类实现****************************

    /**
     * 设置待扫描的路径
     *
     * @return 路径值(以该值开始)
     */
    @NonNull
    protected abstract Set<String> onSetupPkgPathToScan();

}
