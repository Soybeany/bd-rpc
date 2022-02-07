package com.soybeany.sync.core.picker;

/**
 * @author Soybeany
 * @date 2021/12/15
 */
public interface DataPicker<T> extends Iterable<T> {

    /**
     * 设置候选数据
     *
     * @param arr 数据数组
     */
    void set(T[] arr);

    /**
     * 获取下一条数据
     *
     * @return 选中的数据
     */
    T getNext();

    /**
     * 数据不可用时的回调
     *
     * @param data 不可用的数据
     */
    default void onUnusable(T data) {
    }

}
