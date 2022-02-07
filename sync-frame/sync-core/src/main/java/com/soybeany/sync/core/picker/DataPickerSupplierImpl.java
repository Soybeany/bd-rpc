package com.soybeany.sync.core.picker;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * @author Soybeany
 * @date 2022/1/28
 */
@RequiredArgsConstructor
public class DataPickerSupplierImpl<T> implements DataPicker<T> {

    private final Supplier<T> supplier;

    @Override
    public void set(T[] arr) {
        throw new RuntimeException("不支持set方法");
    }

    @Override
    public T getNext() {
        T t = supplier.get();
        return t;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    // ***********************内部类****************************

    private class IteratorImpl implements Iterator<T> {
        private boolean hasNext = true;

        @Override
        public boolean hasNext() {
            if (!hasNext) {
                return false;
            }
            hasNext = false;
            return true;
        }

        @Override
        public T next() {
            return getNext();
        }
    }

}
