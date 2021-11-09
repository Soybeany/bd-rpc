package com.soybeany.rpc.consumer.picker;

import com.soybeany.rpc.core.model.ServerInfo;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * @author Soybeany
 * @date 2021/11/9
 */
public class ServerInfoPickerSeniorImpl extends ServerInfoPickerSimpleImpl {

    private final Map<ServerInfo, Integer> failCountMap = new HashMap<>();
    private final Item[] itemArr;
    private final int valve;
    private final int intervalInMillis;
    private int itemIndex;

    /**
     * @param windowSize    检测的窗口大小，将使用该大小的一半作为概率检测的阈值
     * @param intervalInSec 检测的间隔(秒)
     */
    public ServerInfoPickerSeniorImpl(int windowSize, int intervalInSec) {
        this.itemArr = new Item[windowSize];
        this.valve = windowSize / 2;
        this.intervalInMillis = intervalInSec * 1000;
    }

    @Override
    public synchronized void set(ServerInfo... infoArr) {
        super.set(infoArr);
        Set<ServerInfo> previous = new HashSet<>(failCountMap.keySet());
        // 添加新记录
        for (ServerInfo info : infoArr) {
            if (!failCountMap.containsKey(info)) {
                failCountMap.put(info, 0);
            }
            previous.remove(info);
        }
        // 移除失效记录
        previous.forEach(failCountMap::remove);
    }

    @Override
    public synchronized ServerInfo get() {
        rebase();
        ServerInfo info = super.get();
        // 没有或低于阈值，则不作处理
        int deltaFailCount;
        if (null == info || (deltaFailCount = failCountMap.get(info) - valve) <= 0) {
            return info;
        }
        // 概率返回，失败次数越多则返回值的概率越低
        boolean shouldReturnValue = new Random().nextInt(itemArr.length - valve) >= deltaFailCount;
        return shouldReturnValue ? info : null;
    }

    @Override
    public synchronized void onRequestFailure(ServerInfo info) {
        rebase();
        itemArr[itemIndex].failArr.add(info);
    }

    // ***********************内部方法****************************

    private void rebase() {
        long curId = System.currentTimeMillis() / intervalInMillis;
        Item item = itemArr[itemIndex];
        boolean shouldCreateNewItem = false;
        if (null == item) {
            shouldCreateNewItem = true;
        } else if (item.id != curId) {
            itemIndex = ++itemIndex % itemArr.length;
            shouldCreateNewItem = true;
        }
        if (shouldCreateNewItem) {
            Optional.ofNullable(itemArr[itemIndex])
                    .ifPresent(nextItem -> nextItem.failArr.forEach(this::reduceFailCount));
            itemArr[itemIndex] = new Item(curId);
        }
    }

    private void reduceFailCount(ServerInfo info) {
        Integer count = failCountMap.get(info);
        if (null != count && count > 0) {
            failCountMap.put(info, count - 1);
        }
    }

    // ***********************内部类****************************

    @AllArgsConstructor
    private static class Item {
        final long id;
        final Set<ServerInfo> failArr = new HashSet<>();
    }

}
