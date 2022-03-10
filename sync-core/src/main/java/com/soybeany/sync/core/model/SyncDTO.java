package com.soybeany.sync.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.util.SyncSerializeUtils;
import com.soybeany.util.HexUtils;
import com.soybeany.util.SerializeUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Soybeany
 * @date 2021/11/3
 */
@Data
@RequiredArgsConstructor
public class SyncDTO {

    private final Boolean isNorm;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final SerializeType type;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String dataStr;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String exception;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String errMsg;

    // ***********************静态方法****************************

    public static SyncDTO norm(SerializeType type, Object obj, SyncSerializeUtils.ISerializeHandler handler) throws Exception {
        String data = SyncSerializeUtils.toString(type, obj, handler);
        return new SyncDTO(true, type, data, null, null);
    }

    public static SyncDTO error(Throwable e) {
        try {
            String exception = HexUtils.bytesToHex(SerializeUtils.serialize(e));
            return new SyncDTO(false, null, null, exception, null);
        } catch (IOException e2) {
            return SyncDTO.error("序列化异常信息失败:" + e2.getMessage() + "(" + e2.getClass() + ")");
        }
    }

    public static SyncDTO error(String errMsg) {
        return new SyncDTO(false, null, null, null, errMsg);
    }

    // ***********************成员方法****************************

    public String parseErrMsg() {
        return parseErr().getMessage();
    }

    public Exception parseErr() {
        if (null != exception) {
            try {
                return SerializeUtils.deserialize(HexUtils.hexToByteArray(exception));
            } catch (IOException | ClassNotFoundException e) {
                return new SyncRequestException("反序列化异常信息失败:" + e.getMessage() + "(" + e.getClass() + ")");
            }
        } else if (null != errMsg) {
            return new SyncRequestException(errMsg);
        }
        return new SyncRequestException("没有具体的异常信息");
    }

    public <T> T toData(Type dataType) throws ClassNotFoundException, IOException, SyncException {
        return SyncSerializeUtils.fromString(type, dataType, dataStr);
    }

}
