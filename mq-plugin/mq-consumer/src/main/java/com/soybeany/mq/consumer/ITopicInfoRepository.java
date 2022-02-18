package com.soybeany.mq.consumer;

import com.soybeany.mq.core.model.MqTopicInfo;

import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/2/18
 */
public interface ITopicInfoRepository {

    Collection<MqTopicInfo> getAll(Collection<String> topics);

    void updateTopicInfo(MqTopicInfo info);

}
