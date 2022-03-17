package com.soybeany.rpc.consumer.api;

import com.soybeany.cache.v2.contract.IDatasource;
import com.soybeany.cache.v2.contract.IKeyConverter;
import com.soybeany.cache.v2.core.DataManager;
import com.soybeany.cache.v2.log.ILogWriter;
import com.soybeany.rpc.core.anno.BdRpcCache;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author Soybeany
 * @date 2022/3/17
 */
public interface IRpcDataManagerProvider {

    ILogWriter WRITER = new LogWriter();

    <T> DataManager<T, Object> onGetNew(Method method, BdRpcCache cache, IDatasource<T, Object> dataSource, IKeyConverter<T> keyConverter, String desc, String storageId);

    // ***********************内部类****************************

    @Slf4j
    class LogWriter implements ILogWriter {
        @Override
        public void onWriteInfo(String s) {
            log.info(s);
        }

        @Override
        public void onWriteWarn(String s) {
            log.warn(s);
        }
    }

}
