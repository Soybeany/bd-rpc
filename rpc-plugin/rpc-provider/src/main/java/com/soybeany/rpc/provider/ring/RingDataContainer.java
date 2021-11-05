package com.soybeany.rpc.provider.ring;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/11/5
 */
@Data
@AllArgsConstructor
public class RingDataContainer<T> implements Comparable<RingDataContainer<T>> {

    private T data;
    private long expiryTime;

    @Override
    public int compareTo(RingDataContainer<T> container) {
        return (int) (expiryTime - container.expiryTime);
    }
}
