package com.soybeany.sync.core.picker.filter;

import com.soybeany.sync.core.picker.DataPickerFiltersImpl;

import java.util.*;

/**
 * @author Soybeany
 * @date 2022/2/10
 */
public class DataPickerFuseFilter<T> implements DataPickerFiltersImpl.Filter<T> {

    private final Map<T, Integer> failCountMap = new HashMap<>();
    private final List<Set<T>> failRecords = new ArrayList<>();
    private final int valve;
    private final int testBound;
    private final int intervalInMillis;
    private int curIndex;
    private long lastPeriod;

    public DataPickerFuseFilter() {
        this(10, 0.5f, 10);
    }

    /**
     * @param windowSize    检测的窗口大小
     * @param valveRate     阈值比例，当失败值高于此比例时，将开启熔断判断
     * @param intervalInSec 检测的间隔(秒)
     */
    public DataPickerFuseFilter(int windowSize, float valveRate, int intervalInSec) {
        if (windowSize <= 0) {
            throw new RuntimeException("windowSize需大于0");
        }
        if (valveRate < 0 || valveRate >= 1) {
            throw new RuntimeException("valveRate需大于等于0且小于1");
        }
        if (intervalInSec <= 0) {
            throw new RuntimeException("intervalInSec需大于0");
        }
        for (int i = 0; i < windowSize; i++) {
            this.failRecords.add(new HashSet<>());
        }
        this.valve = (int) (windowSize * valveRate);
        this.testBound = windowSize - valve;
        this.intervalInMillis = intervalInSec * 1000;
    }

    @Override
    public void onSet(List<T> available) {
        Set<Object> newData = new HashSet<>(available);
        // 移除失效记录
        failCountMap.keySet().removeIf(o -> !newData.contains(o));
    }

    @Override
    public void onGetNext(List<T> available) {
        rebase();
    }

    @Override
    public void onGetAllUsable(List<T> available) {
        rebase();
    }

    @Override
    public void onUnusable(T data) {
        rebase();
        failRecords.get(curIndex).add(data);
        failCountMap.put(data, Optional.ofNullable(failCountMap.get(data)).orElse(0) + 1);
    }

    @Override
    public boolean shouldFilter(T data) {
        // 没有或低于阈值，则不作处理
        Integer failCount = failCountMap.get(data);
        int denyRange;
        if (null == failCount || (denyRange = failCount - valve) <= 0) {
            return false;
        }
        // 概率过滤，失败次数越多则拒绝范围越大，对应的过滤概率则越高
        int bound = new Random().nextInt(testBound);
        System.out.println("bound:" + bound + " denyRange:" + denyRange);
        return bound < denyRange;
    }

    // ***********************内部方法****************************

    private void rebase() {
        long curPeriod = System.currentTimeMillis() / intervalInMillis;
        // 若周期无变化，则不需作处理
        if (lastPeriod == curPeriod) {
            return;
        }
        // 计算周期差值
        int deltaPeriod = (int) Math.min(curPeriod - lastPeriod, failRecords.size());
        // 清空周期差间的记录，并减少相应的计数
        for (int i = 0; i < deltaPeriod; i++) {
            curIndex = ++curIndex % failRecords.size();
            Set<T> set = failRecords.get(curIndex);
            reduceFailCount(set);
            set.clear();
        }
        // 更新period
        lastPeriod = curPeriod;
    }

    private void reduceFailCount(Set<T> set) {
        for (T data : set) {
            Integer failCount = failCountMap.get(data);
            if (null == failCount) {
                continue;
            }
            if (failCount > 1) {
                failCountMap.put(data, failCount - 1);
            } else {
                failCountMap.remove(data);
            }
        }
    }

}
