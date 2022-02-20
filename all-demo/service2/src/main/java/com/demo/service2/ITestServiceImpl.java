package com.demo.service2;

import com.demo.service1.model.ExIoException;
import com.demo.service1.model.TestVO;
import com.demo.service2.model.TestParam;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.util.SerializeUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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

    public static void main(String[] args) throws Exception {
        ExIoException child = new ExIoException("sdf");
//        child.string = "sdfe";
        RpcServerInfo field = new RpcServerInfo();
        field.setInvokeUrl("sdf");
//        child.field = field;
        child.onSetupServerInfo(field);
        byte[] bytes = SerializeUtils.serialize(child);
        ExIoException c = SerializeUtils.deserialize(bytes);
        int d = 2;
    }

    private static final class Child implements Serializable {
        public String string;
        public RpcServerInfo field;
    }

    private static final class Super {

    }

    @Data
    private static final class Field {
        public String value;
    }

}
