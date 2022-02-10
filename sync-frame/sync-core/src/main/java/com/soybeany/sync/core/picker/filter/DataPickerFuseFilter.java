package com.soybeany.sync.core.picker.filter;

import com.soybeany.sync.core.picker.DataPickerFiltersImpl;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * @author Soybeany
 * @date 2022/2/10
 */
public class DataPickerFuseFilter<T> implements DataPickerFiltersImpl.Filter<T> {

    private final Map<Object, Integer> failCountMap = new HashMap<>();
    private final Item[] itemArr;
    private final int valve;
    private final int intervalInMillis;
    private int itemIndex;

    /**
     * @param windowSize    检测的窗口大小，将使用该大小的一半作为概率检测的阈值
     * @param intervalInSec 检测的间隔(秒)
     */
    public DataPickerFuseFilter(int windowSize, int intervalInSec) {
        this.itemArr = new Item[windowSize];
        this.valve = windowSize / 2;
        this.intervalInMillis = intervalInSec * 1000;
    }

    @Override
    public void onSet(List<T> available) {
        Set<Object> previous = new HashSet<>(failCountMap.keySet());
        // 添加新记录
        for (T data : available) {
            if (!failCountMap.containsKey(data)) {
                failCountMap.put(data, 0);
            }
            previous.remove(data);
        }
        // 移除失效记录
        previous.forEach(failCountMap::remove);
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
    public synchronized void onUnusable(T data) {
        rebase();
        itemArr[itemIndex].failArr.add(data);
    }

    @Override
    public boolean shouldFilter(T data) {
        // 没有或低于阈值，则不作处理
        Integer failCount = failCountMap.get(data);
        int deltaFailCount;
        if (null == failCount || (deltaFailCount = failCount - valve) <= 0) {
            return false;
        }
        // 概率返回，失败次数越多则返回值的概率越低
        return new Random().nextInt(itemArr.length - valve) < deltaFailCount;
    }

    // ***********************内部方法****************************

    private synchronized void rebase() {
        long curId = System.currentTimeMillis() / intervalInMillis;
        Item item = itemArr[itemIndex];
        boolean shouldCreateNewItem = false;
        if (null == item) {
            shouldCreateNewItem = true;
        } else if (item.id != curId) {
            itemIndex = ++itemIndex % itemArr.length;
            shouldCreateNewItem = true;
        }
        if (shouldCreateNewItem) {
            Optional.ofNullable(itemArr[itemIndex])
                    .ifPresent(nextItem -> nextItem.failArr.forEach(this::reduceFailCount));
            itemArr[itemIndex] = new Item(curId);
        }
    }

    private void reduceFailCount(Object data) {
        Integer count = failCountMap.get(data);
        if (null != count && count > 0) {
            failCountMap.put(data, count - 1);
        }
    }

    // ***********************内部类****************************

    @AllArgsConstructor
    private static class Item {
        final long id;
        final Set<Object> failArr = new HashSet<>();
    }

}
