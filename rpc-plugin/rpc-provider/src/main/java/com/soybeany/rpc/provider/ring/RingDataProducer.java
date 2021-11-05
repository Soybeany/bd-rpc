package com.soybeany.rpc.provider.ring;

/**
 * @author Soybeany
 * @date 2021/11/5
 */
public interface RingDataProducer<T> {

    T onGetNew();

}
