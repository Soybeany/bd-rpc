package com.soybeany.sync.core.picker;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 使用提供者动态提供
 *
 * @author Soybeany
 * @date 2022/1/28
 */
@RequiredArgsConstructor
public class DataPickerSupplierImpl<T> implements DataPicker<T> {

    private final Supplier<T> supplier;

    @Override
    public void set(List<T> list) {
        throw new RuntimeException("不支持set方法");
    }

    @Override
    public T getNext() {
        return supplier.get();
    }

    @Override
    public List<T> getAllUsable() {
        return Collections.singletonList(getNext());
    }

}
