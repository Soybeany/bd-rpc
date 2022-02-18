package com.soybeany.mq.consumer;

import com.soybeany.mq.core.model.MqTopicInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/2/18
 */
public class TopicInfoRepositoryMemImpl implements ITopicInfoRepository {

    private final Map<String, MqTopicInfo> storage = new HashMap<>();

    @Override
    public Collection<MqTopicInfo> getAll(Collection<String> topics) {
        Collection<MqTopicInfo> result = new ArrayList<>();
        for (String topic : topics) {
            MqTopicInfo info = storage.get(topic);
            if (null == info) {
                info = new MqTopicInfo(topic, 0L);
            }
            result.add(info);
        }
        return result;
    }

    @Override
    public void updateTopicInfo(MqTopicInfo info) {
        storage.put(info.getTopic(), info);
    }

}
