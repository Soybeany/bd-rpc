package com.soybeany.mq.registry;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqClientInputR;
import com.soybeany.mq.core.model.MqClientOutputR;
import com.soybeany.util.Md5Utils;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqRegistryPluginC extends MqRegistryPlugin<MqClientOutputR, MqClientInputR> {

    private final IStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C_R;
    }

    @Override
    public Class<MqClientOutputR> onGetInputClass() {
        return MqClientOutputR.class;
    }

    @Override
    public Class<MqClientInputR> onGetOutputClass() {
        return MqClientInputR.class;
    }

    @Override
    public void onHandleSync(MqClientOutputR in, MqClientInputR out) {
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
