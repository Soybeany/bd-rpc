package com.soybeany.sync.core.picker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 使用遍历算法
 *
 * @author Soybeany
 * @date 2021/10/29
 */
public class DataPickerSimpleImpl<T> implements DataPicker<T> {

    private List<T> dataList;
    private int curIndex;

    public DataPickerSimpleImpl() {
        set(Collections.emptyList());
    }

    @SafeVarargs
    public DataPickerSimpleImpl(T... arr) {
        set(Arrays.asList(arr));
    }

    @Override
    public synchronized void set(List<T> list) {
        this.dataList = list;
        curIndex = -1;
    }

    @Override
    public synchronized T getNext() throws NoNextDataException {
        if (hasNoDataList()) {
            throw new NoNextDataException();
        }
        switchToNextIndex();
        return dataList.get(curIndex);
    }

    @Override
    public synchronized List<T> getAllUsable() {
        if (hasNoDataList()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(dataList);
    }

    // ***********************内部方法****************************

    private boolean hasNoDataList() {
        return null == dataList || dataList.isEmpty();
    }

    private void switchToNextIndex() {
        curIndex = (curIndex + 1) % dataList.size();
    }

}
