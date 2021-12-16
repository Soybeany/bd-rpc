package com.soybeany.sync.core.picker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 使用遍历算法
 *
 * @author Soybeany
 * @date 2021/10/29
 */
public class DataPickerSimpleImpl<T> implements DataPicker<T> {

    private List<T> infoArr;
    private int curIndex;

    @Override
    public synchronized void set(T[] arr) {
        set(Arrays.asList(arr));
    }

    @Override
    public synchronized T getNext() {
        if (null == infoArr || infoArr.size() <= 0) {
            return null;
        }
        return infoArr.get(getNextIndex());
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return new IteratorImpl(getNextIndex());
    }

    // ***********************内部方法****************************

    private void set(List<T> list) {
        this.infoArr = list;
        curIndex = -1;
    }

    private int getNextIndex() {
        if (infoArr.isEmpty()) {
            return -1;
        }
        return ++curIndex % infoArr.size();
    }

    // ***********************内部类****************************

    private class IteratorImpl implements Iterator<T> {

        private final DataPickerSimpleImpl<T> picker = new DataPickerSimpleImpl<>();
        private int usedCount;

        private IteratorImpl(int startIndex) {
            picker.set(new ArrayList<>(infoArr));
            picker.curIndex = startIndex;
        }

        @Override
        public boolean hasNext() {
            return usedCount < picker.infoArr.size();
        }

        @Override
        public T next() {
            usedCount++;
            return picker.getNext();
        }

    }

}
