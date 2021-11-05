package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.provider.ring.RingDataProducer;
import com.soybeany.util.file.BdFileUtils;

/**
 * @author Soybeany
 * @date 2021/11/6
 */
public class AuthorizationProducer implements RingDataProducer<String> {
    @Override
    public String onGetNew() {
        System.out.println("获取新数据");
        return BdFileUtils.getUuid();
    }
}
