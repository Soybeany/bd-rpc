package com.soybeany.rpc.core.model;

import com.soybeany.rpc.core.exception.RpcRequestException;
import com.soybeany.util.HexUtils;
import com.soybeany.util.SerializeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/11/3
 */
@Data
@AllArgsConstructor
public class RpcDTO {

    private Boolean isNorm;
    private String dataJson;
    private String exception;
    private String errMsg;

    public static RpcDTO norm(Object obj) {
        return new RpcDTO(true, GSON.toJson(obj), null, null);
    }

    public static RpcDTO error(Throwable e) throws IOException {
        return new RpcDTO(false, null, HexUtils.bytesToHex(SerializeUtils.serialize(e)), null);
    }

    public static RpcDTO error(String errMsg) {
        return new RpcDTO(false, null, null, errMsg);
    }

    public <T> T getData(Type type) {
        return GSON.fromJson(dataJson, type);
    }

    public <T> T throwException() throws Throwable {
        if (null != exception) {
            Throwable throwable;
            try {
                throwable = SerializeUtils.deserialize(HexUtils.hexToByteArray(exception));
            } catch (IOException | ClassNotFoundException e) {
                throw new RpcRequestException("反序列化异常信息失败:" + e.getMessage());
            }
            throw throwable;
        } else if (null != errMsg) {
            throw new RpcRequestException(errMsg);
        }
        throw new RpcRequestException("没有具体的异常信息");
    }

}
