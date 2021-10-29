package com.soybeany.rpc.model;

import lombok.Data;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class ConsumerParam {

    /**
     * 所需资源的id列表
     */
    private List<String> resourceIdList;

}
