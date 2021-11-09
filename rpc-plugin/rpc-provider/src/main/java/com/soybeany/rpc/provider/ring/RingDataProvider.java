package com.soybeany.rpc.provider.ring;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Soybeany
 * @date 2021/11/5
 */
public class RingDataProvider<T> {

    private final Builder<T> b;
    private final RingDataContainer<T>[] containers;
    private final int targetLength;
    private int curIndex;

    private boolean loaded;

    private RingDataProvider(Builder<T> builder) {
        this.b = builder;
        this.targetLength = b.preCount + b.nextCount + 1;
        //noinspection unchecked
        this.containers = new RingDataContainer[targetLength];
    }

    public synchronized T getNewest() throws DataModifiedException {
        if (!loaded) {
            init();
            loaded = true;
        }
        try {
            check();
        } catch (DataModifiedException e) {
            // 遇到数据变更，自动初始化一次
            init();
            check();
        }
        return containers[curIndex].getData();
    }

    /**
     * 判断指定数据是否有效
     */
    public synchronized boolean isValid(T obj) {
        long curTime = System.currentTimeMillis();
        for (RingDataContainer<T> container : containers) {
            if (!Objects.equals(obj, container.getData())) {
                continue;
            }
            if (container.getExpiryTime() > curTime) {
                return true;
            }
        }
        return false;
    }

    // ***********************内部方法****************************

    private void check() throws DataModifiedException {
        long curTime = System.currentTimeMillis();
        // 确定有效位置
        int maxIndex = curIndex + targetLength, targetIndex;
        for (targetIndex = curIndex; targetIndex < maxIndex; targetIndex++) {
            int index = targetIndex % targetLength;
            if (isDataValid(containers[index], curTime)) {
                break;
            }
        }
        // 无效位置填充新数据
        boolean shouldSave = false;
        for (int i = targetIndex - 1; i >= curIndex; i--) {
            long expiryTime = curTime + (targetLength - targetIndex + i + 1) * b.refreshIntervalMillis;
            containers[i % targetLength] = new RingDataContainer<>(b.producer.onGetNew(), expiryTime);
            shouldSave = true;
        }
        // 有创建新数据，则保存
        if (shouldSave) {
            b.dao.onSave(Arrays.asList(containers));
            curIndex = targetIndex % targetLength;
        }
    }

    private void init() {
        Arrays.fill(containers, null);
        curIndex = 0;
        // 读取记录并排序
        List<RingDataContainer<T>> list = b.dao.onLoad();
        Collections.sort(list);
        int min = Math.min(list.size(), targetLength);
        for (int i = 0; i < min; i++) {
            containers[i] = list.get(i);
        }
    }

    private boolean isDataValid(RingDataContainer<T> container, long curTime) {
        return null != container && container.getExpiryTime() - curTime > 0;
    }

    // ***********************内部类****************************

    @Setter
    @Accessors(fluent = true, chain = true)
    public static class Builder<T> {

        private final RingDataProducer<T> producer;
        private final RingDataDAO<T> dao;
        private final long refreshIntervalMillis;
        private int preCount = 1;
        private int nextCount = 1;

        public Builder(RingDataProducer<T> producer, RingDataDAO<T> dao, long refreshIntervalMillis) {
            this.producer = producer;
            this.dao = dao;
            this.refreshIntervalMillis = refreshIntervalMillis;
        }

        public RingDataProvider<T> build() {
            return new RingDataProvider<>(this);
        }
    }

}
