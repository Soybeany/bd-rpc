package com.soybeany.sync.core.picker;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用一组过滤器过滤
 *
 * @author Soybeany
 * @date 2022/2/10
 */
@RequiredArgsConstructor
public class DataPickerFiltersImpl<T> extends DataPickerSimpleImpl<T> {

    private final List<Filter<T>> filters;

    @Override
    public synchronized void set(List<T> list) {
        super.set(list);
        filters.forEach(filter -> filter.onSet(list));
    }

    @Override
    public synchronized T getNext() throws NoNextDataException {
        List<T> available = super.getAllUsable();
        filters.forEach(filter -> filter.onGetNext(available));
        out:
        for (T data : available) {
            for (Filter<T> filter : filters) {
                if (filter.shouldFilter(data)) {
                    continue out;
                }
            }
            return data;
        }
        throw new NoNextDataException();
    }

    @Override
    public synchronized List<T> getAllUsable() {
        List<T> available = super.getAllUsable();
        filters.forEach(filter -> filter.onGetAllUsable(available));
        List<T> result = new ArrayList<>();
        out:
        for (T data : available) {
            for (Filter<T> filter : filters) {
                if (filter.shouldFilter(data)) {
                    continue out;
                }
            }
            result.add(data);
        }
        return result;
    }

    @Override
    public synchronized void onUnusable(T data) {
        filters.forEach(filter -> filter.onUnusable(data));
    }

    // ***********************内部类****************************

    public interface Filter<T> {

        default void onSet(List<T> available) {
        }

        default void onGetNext(List<T> available) {
        }

        default void onGetAllUsable(List<T> available) {
        }

        default void onUnusable(T data) {
        }

        boolean shouldFilter(T data);

    }

}
