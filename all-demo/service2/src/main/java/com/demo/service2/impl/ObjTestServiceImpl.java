package com.demo.service2.impl;

import com.demo.model.Interface;
import com.demo.model.ResultImpl;
import com.demo.model.ResultVO;
import com.demo.model.TestParam;
import com.demo.service.IObjTestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Service
public class ObjTestServiceImpl implements IObjTestService {
    @Override
    public List<ResultVO> getValue(List<TestParam> param) {
        return param.stream()
                .map(p -> new ResultVO(p.getA() + "+" + p.getB()))
                .collect(Collectors.toList());
    }

    @Override
    public Interface getValue(Interface i) {
        return new ResultImpl("service2结果");
    }
}
