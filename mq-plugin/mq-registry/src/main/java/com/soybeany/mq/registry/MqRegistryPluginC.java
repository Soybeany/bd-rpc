package com.soybeany.mq.registry;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.registry.MqClientInput;
import com.soybeany.mq.core.model.registry.MqClientOutput;
import com.soybeany.util.Md5Utils;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqRegistryPluginC extends MqRegistryPlugin<MqClientOutput, MqClientInput> {

    private final IMqStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C_R;
    }

    @Override
    public Class<MqClientOutput> onGetInputClass() {
        return MqClientOutput.class;
    }

    @Override
    public Class<MqClientInput> onGetOutputClass() {
        return MqClientInput.class;
    }

    @Override
    public void onHandleSync(MqClientOutput in, MqClientInput out) {
        Set<String> syncUrls = storageManager.load(in.getSystem());
        // 当md5不一致时，再返回数据
        String md5 = Md5Utils.strToMd5(GSON.toJson(syncUrls));
        if (!md5.equals(in.getMd5())) {
            out.setUpdated(true);
            out.setMd5(md5);
            out.setBrokersSyncUrl(syncUrls.toArray(new String[0]));
        }
    }
}
