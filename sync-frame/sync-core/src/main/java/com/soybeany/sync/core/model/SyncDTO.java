package com.soybeany.sync.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.util.HexUtils;
import com.soybeany.util.SerializeUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Type;

import static com.soybeany.sync.core.util.NetUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/11/3
 */
@Data
@RequiredArgsConstructor
public class SyncDTO {

    private final Boolean isNorm;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final Boolean useSerialize;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String dataStr;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String exception;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String errMsg;

    // ***********************静态方法****************************

    public static SyncDTO norm(boolean useSerialize, Object obj, IExceptionHandler handler) throws Exception {
        String data = null;
        if (null != obj) {
            data = useSerialize ? serialize(obj, handler) : toJson(obj);
        }
        return new SyncDTO(true, useSerialize, data, null, null);
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

    // ***********************内部静态方法****************************

    private static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    private static String serialize(Object obj, IExceptionHandler handler) throws Exception {
        try {
            return HexUtils.bytesToHex(SerializeUtils.serialize(obj));
        } catch (NotSerializableException e) {
            throw handler.onNotSerializable(e);
        }
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

    public <T> T toData(Type type) throws ClassNotFoundException, IOException {
        if (null == dataStr) {
            return null;
        }
        return useSerialize ? deserialize(dataStr) : fromJson(dataStr, type);
    }

    // ***********************内部成员方法****************************

    private <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    private <T> T deserialize(String hex) throws ClassNotFoundException, IOException {
        return SerializeUtils.deserialize(HexUtils.hexToByteArray(hex));
    }

    // ***********************内部类****************************

    public interface IExceptionHandler {
        Exception onNotSerializable(NotSerializableException e);
    }

}
