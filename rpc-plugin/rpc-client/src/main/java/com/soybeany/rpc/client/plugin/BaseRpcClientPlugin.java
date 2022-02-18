package com.soybeany.rpc.client.plugin;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcFallback;
import com.soybeany.sync.client.api.IClientPlugin;

import java.util.*;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public abstract class BaseRpcClientPlugin<Input, Output> implements IClientPlugin<Input, Output> {

    private final List<String> exPkgPathsToScan = new ArrayList<>();
    private List<String> postTreatPkgPathsToScan;

    protected static String getId(String version, BdRpc annotation) {
        return annotation.serviceId() + "-" + version;
    }

    protected static boolean isFallbackImpl(Object obj) {
        return null != obj.getClass().getAnnotation(BdRpcFallback.class);
    }

    protected List<String> getPostTreatPkgPathsToScan() {
        if (null == postTreatPkgPathsToScan) {
            List<String> paths = Optional
                    .ofNullable(onSetupPkgPathToScan())
                    .map(ArrayList::new)
                    .orElseGet(ArrayList::new);
            paths.addAll(exPkgPathsToScan);
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

    public void addExPkgPathsToScan(List<String> paths) {
        exPkgPathsToScan.addAll(paths);
    }

    // ***********************子类实现****************************

    /**
     * 设置待扫描的路径
     *
     * @return 路径值(以该值开始)
     */
    protected abstract Set<String> onSetupPkgPathToScan();

}
