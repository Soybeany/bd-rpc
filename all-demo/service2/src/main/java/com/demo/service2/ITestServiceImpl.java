package com.demo.service2;

import com.demo.service1.model.ExIoException;
import com.demo.service1.model.TestVO;
import com.demo.service2.model.TestParam;
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
            TestVO vo = new TestVO();
            vo.setValue(param.getB() + param.getA());
            result.add(vo);
        }
        log.info("调用了数据提供者");
        return result;
    }

    @Override
    public String getValue2() throws ExIoException {
        throw new ExIoException("好");
    }

    @Override
    public String getValue3() {
        return "数据3";
    }

    @Override
    public String getValue4() {
        return "数据4";
    }

    @Override
    public String getValue5() {
        return "数据5";
    }

    @Override
    public String getBatchValue(String input) {
        return "来自Service2:" + input;
    }
}
