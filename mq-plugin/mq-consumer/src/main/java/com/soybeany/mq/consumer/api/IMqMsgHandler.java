package com.soybeany.mq.consumer.api;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static com.soybeany.sync.core.util.NetUtils.GSON;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqMsgHandler {

    String onSetupTopic();

    void onHandle(List<String> messages) throws Exception;

    abstract class JsonMsg<T> implements IMqMsgHandler {

        @SuppressWarnings("unchecked")
        @Override
        public void onHandle(List<String> messages) {
            onHandleObjects(messages.stream()
                    .map(msg -> (T) GSON.fromJson(msg, onSetupObjType()))
                    .collect(Collectors.toList()));
        }

        protected abstract void onHandleObjects(List<T> objects);

        protected abstract Type onSetupObjType();

    }

}
