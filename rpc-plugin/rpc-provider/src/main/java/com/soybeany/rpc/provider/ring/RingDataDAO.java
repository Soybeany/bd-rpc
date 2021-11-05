package com.soybeany.rpc.provider.ring;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/11/5
 */
public interface RingDataDAO<T> {

    void onSave(List<RingDataContainer<T>> data) throws DataModifiedException;

    List<RingDataContainer<T>> onLoad();


    class MemImpl<T> implements RingDataDAO<T> {

        private final List<RingDataContainer<T>> list = new LinkedList<>();

        @Override
        public synchronized void onSave(List<RingDataContainer<T>> data) {
            list.clear();
            list.addAll(data);
        }

        @Override
        public synchronized List<RingDataContainer<T>> onLoad() {
            return list;
        }
    }

}
