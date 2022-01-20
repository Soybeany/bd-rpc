package com.soybeany.demo;

import com.soybeany.mq.producer.BaseMqMsgSenderImpl;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Component
public class MqMsgSenderImpl extends BaseMqMsgSenderImpl {

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
