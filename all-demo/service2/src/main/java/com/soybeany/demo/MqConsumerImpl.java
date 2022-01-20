package com.soybeany.demo;

import com.soybeany.mq.consumer.BaseMqConsumerImpl;
import com.soybeany.mq.consumer.IMqExceptionHandler;
import com.soybeany.mq.core.api.IMqMsgHandler;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Component
public class MqConsumerImpl extends BaseMqConsumerImpl {

    @Autowired
    private List<IMqMsgHandler> handlers;

    @Override
    protected List<IMqMsgHandler> onSetupMsgHandlers() {
        return handlers;
    }

    @Override
    protected IMqExceptionHandler onSetupExceptionHandler() {
        return null;
    }

    @Override
    public DataPicker<String> onGetSyncServerPicker() {
        return new DataPickerSimpleImpl<>("http://localhost:8080/bd-api/mqSync");
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }

    @PostConstruct
    private void onInit() {
        start();
    }

    @PreDestroy
    private void onDestroy() {
        stop();
    }

}
