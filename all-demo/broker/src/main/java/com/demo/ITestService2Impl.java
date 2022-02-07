package com.demo;

import com.demo.model.TestParam;
import com.demo.model.TestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Slf4j
@Component
public class ITestService2Impl implements ITestService2 {

    @Override
    public List<TestVO> getValue(List<TestParam> param) {
        TestVO vo = new TestVO();
        vo.setValue("broker");
        return Collections.singletonList(vo);
    }

    @Override
    public String getBatchValue(String input) {
        return "来自broker:" + input;
    }

}
