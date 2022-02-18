package com.soybeany.sync.client.picker;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/15
 */
public interface DataPicker<T> {

    /**
     * 设置候选数据
     *
     * @param list 数据列表
     */
    void set(List<T> list);

    /**
     * 获取下一条数据
     *
     * @return 选中的数据
     */
    T getNext() throws NoNextDataException;

    /**
     * 获取全部可用数据
     *
     * @return 可用数据列表
     */
    List<T> getAllUsable();

    /**
     * 数据不可用时的回调
     *
     * @param data 不可用的数据
     */
    default void onUnusable(T data) {
    }

    class NoNextDataException extends Exception {
    }

}
