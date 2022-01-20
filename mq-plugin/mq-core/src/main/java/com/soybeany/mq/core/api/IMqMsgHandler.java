package com.soybeany.mq.core.api;

import java.lang.reflect.Type;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqMsgHandler {

    String onSetupTopic();

    void onHandle(String msg) throws Exception;

    abstract class JsonMsg<T> implements IMqMsgHandler {

        @SuppressWarnings("unchecked")
        @Override
        public void onHandle(String msg) {
            onHandle((T) GSON.fromJson(msg, onSetupObjType()));
        }

        protected abstract void onHandle(T obj);

        protected abstract Type onSetupObjType();

    }

}
