package com.soybeany.demo;

import com.soybeany.demo.model.TestParam;
import com.soybeany.demo.model.TestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Slf4j
@Component
public class ITestServiceImpl implements ITestService {
    @Override
    public List<TestVO> getValue(List<TestParam> params) {
        List<TestVO> result = new LinkedList<>();
        for (TestParam param : params) {
            result.add(new TestVO(param.getB() + param.getA()));
        }
        log.info("调用了数据提供者");
        return result;
    }
}
