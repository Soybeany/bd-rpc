package com.soybeany.rpc.provider.plugin;

import com.soybeany.rpc.client.model.RpcMethodInfo;
import com.soybeany.rpc.client.plugin.BaseRpcClientPlugin;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcProviderInput;
import com.soybeany.rpc.core.model.RpcProviderOutput;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.rpc.provider.api.IRpcServiceExecutor;
import com.soybeany.rpc.provider.util.ReflectUtils;
import com.soybeany.sync.client.model.SyncClientInfo;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.util.Md5Utils;
import com.soybeany.util.file.BdFileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.NetUtils.GSON;

/**
 * todo 加入方法执行情况统计信息，加入到{@link RpcServerInfo}中，可用于作调用情况的负载均衡
 *
 * @author Soybeany
 * @date 2021/10/27
 */
@RequiredArgsConstructor
public class RpcProviderPlugin extends BaseRpcClientPlugin<RpcProviderInput, RpcProviderOutput> implements IRpcServiceExecutor {

    private final Map<String, Object> serviceMap = new HashMap<>();
    private final String authorizationToken = BdFileUtils.getUuid();
    private final RpcServerInfo rpcServerInfo = new RpcServerInfo();

    private final String system;
    private final String version;
    private final String group;
    private final ApplicationContext appContext;
    private final String invokeUrl;
    private final Set<String> pkgToScan;

    private String md5;

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG_P;
    }

    @Override
    public Class<RpcProviderInput> onGetInputClass() {
        return RpcProviderInput.class;
    }

    @Override
    public Class<RpcProviderOutput> onGetOutputClass() {
        return RpcProviderOutput.class;
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Override
    public void onStartup(SyncClientInfo info) {
        super.onStartup(info);
        // 配置服务器信息
        rpcServerInfo.setGroup(group);
        rpcServerInfo.setInvokeUrl(invokeUrl);
        rpcServerInfo.setAuthorization(authorizationToken);
        // 扫描
        new Thread(() -> {
            List<String> paths = getPostTreatPkgPathsToScan();
            for (String name : appContext.getBeanDefinitionNames()) {
                Object bean = appContext.getBean(name);
                for (String path : paths) {
                    Optional.ofNullable(ReflectUtils.getAnnotation(path, BdRpc.class, bean.getClass()))
                            .ifPresent(bdRpc -> onHandleBean(bdRpc, bean));
                }
            }
        }).start();
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, RpcProviderOutput output) throws Exception {
        output.setProviderId(system + "-" + rpcServerInfo.getInvokeUrl());
        Set<String> serviceIds = serviceMap.keySet();
        String md5 = Md5Utils.strToMd5(GSON.toJson(new Object[]{rpcServerInfo, serviceIds}));
        if (!md5.equals(this.md5)) {
            output.setUpdated(true);
            output.setSystem(system);
            output.setMd5(md5);
            output.setRpcServerInfo(rpcServerInfo);
            output.setServiceIds(serviceIds);
        }
        return super.onBeforeSync(uid, output);
    }

    @Override
    public synchronized void onAfterSync(String uid, RpcProviderInput input) throws Exception {
        super.onAfterSync(uid, input);
        md5 = input.getMd5();
    }

    @Override
    protected Set<String> onSetupPkgPathToScan() {
        return pkgToScan;
    }

    @Override
    public SyncDTO execute(HttpServletRequest request, HttpServletResponse response) {
        // 凭证校验
        if (!authorizationToken.equals(request.getHeader(HEADER_AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        // 处理rpc调用
        try {
            String param = request.getParameter(KEY_METHOD_INFO);
            RpcMethodInfo info = GSON.fromJson(param, RpcMethodInfo.class);
            return SyncDTO.norm(info.getReturnType(), onExecuteSingle(info), e -> {
                Object obj = serviceMap.get(info.getServiceId());
                return new RpcPluginException("类“" + obj.getClass() + "”中方法“" + info.getMethodDesc() + "”的返回值中含有不可序列化的对象“" + e.getMessage() + "”");
            });
        } catch (Throwable throwable) {
            return SyncDTO.error(throwable);
        }
    }

    // ***********************内部方法****************************

    private Object onExecuteSingle(RpcMethodInfo info) throws Throwable {
        Object obj = serviceMap.get(info.getServiceId());
        if (null == obj) {
            throw new RpcPluginException("没有找到指定的服务(" + info.getServiceId() + ")");
        }
        Method method = info.getMethod(obj);
        try {
            return method.invoke(obj, info.getArgs(method));
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private void onHandleBean(BdRpc bdRpc, Object bean) {
        // 熔断的实现不作为提供者
        if (isFallbackImpl(bean)) {
            return;
        }
        // 其余情况才作为提供者
        String id = getId(version, bdRpc);
        Object previous = serviceMap.put(id, bean);
        if (null != previous) {
            throw new RpcPluginException("@BdRpc的serviceId(" + id + ")需唯一");
        }
    }

}
