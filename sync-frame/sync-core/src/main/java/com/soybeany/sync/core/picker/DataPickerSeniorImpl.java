package com.soybeany.sync.core.picker;

import lombok.AllArgsConstructor;

import java.util.*;

/**
 * 使用滑动窗口算法
 *
 * @author Soybeany
 * @date 2021/11/9
 */
public class DataPickerSeniorImpl<T> extends DataPickerSimpleImpl<T> {

    private final Map<T, Integer> failCountMap = new HashMap<>();
    private final List<Item<T>> itemList;
    private final int valve;
    private final int intervalInMillis;
    private int itemIndex;
    private int modCount;

    /**
     * @param windowSize    检测的窗口大小，将使用该大小的一半作为概率检测的阈值
     * @param intervalInSec 检测的间隔(秒)
     */
    public DataPickerSeniorImpl(int windowSize, int intervalInSec) {
        this.itemList = new ArrayList<>(windowSize);
        this.valve = windowSize / 2;
        this.intervalInMillis = intervalInSec * 1000;
    }

    @Override
    public synchronized void set(T[] arr) {
        super.set(arr);
        modCount++;
        Set<T> previous = new HashSet<>(failCountMap.keySet());
        // 添加新记录
        for (T data : arr) {
            if (!failCountMap.containsKey(data)) {
                failCountMap.put(data, 0);
            }
            previous.remove(data);
        }
        // 移除失效记录
        previous.forEach(failCountMap::remove);
    }

    @Override
    public synchronized T getNext() {
        rebase();
        return getData(super.getNext());
    }

    @Override
    public synchronized void onUnusable(T data) {
        rebase();
        itemList.get(itemIndex).failArr.add(data);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return new IteratorImpl(super.iterator());
    }

    // ***********************内部方法****************************

    private void rebase() {
        long curId = System.currentTimeMillis() / intervalInMillis;
        Item<T> item = itemList.get(itemIndex);
        boolean shouldCreateNewItem = false;
        if (null == item) {
            shouldCreateNewItem = true;
        } else if (item.id != curId) {
            itemIndex = ++itemIndex % itemList.size();
            shouldCreateNewItem = true;
        }
        if (shouldCreateNewItem) {
            Optional.ofNullable(itemList.get(itemIndex))
                    .ifPresent(nextItem -> nextItem.failArr.forEach(this::reduceFailCount));
            itemList.set(itemIndex, new Item<>(curId));
        }
    }

    private void reduceFailCount(T data) {
        Integer count = failCountMap.get(data);
        if (null != count && count > 0) {
            failCountMap.put(data, count - 1);
        }
    }

    private T getData(T data) {
        // 没有或低于阈值，则不作处理
        int deltaFailCount;
        if (null == data || (deltaFailCount = failCountMap.get(data) - valve) <= 0) {
            return data;
        }
        // 概率返回，失败次数越多则返回值的概率越低
        boolean shouldReturnValue = new Random().nextInt(itemList.size() - valve) >= deltaFailCount;
        return shouldReturnValue ? data : null;
    }

    // ***********************内部类****************************

    @AllArgsConstructor
    private static class Item<T> {
        final long id;
        final Set<T> failArr = new HashSet<>();
    }

    private class IteratorImpl implements Iterator<T> {

        private final Iterator<T> iterator;
        private final int expectedModCount;

        private IteratorImpl(Iterator<T> iterator) {
            this.iterator = iterator;
            expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return getData(iterator.next());
        }
    }

}
