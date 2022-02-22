package com.demo.service1.fallback;

import com.demo.service.IFallbackTestService;
import com.soybeany.rpc.core.anno.BdRpcFallback;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
@BdRpcFallback
@Component
public class FallbackImpl implements IFallbackTestService {

    @Override
    public String getValue() {
        return "service1熔断";
    }

}
