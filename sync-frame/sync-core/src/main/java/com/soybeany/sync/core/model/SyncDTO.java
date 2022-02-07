package com.soybeany.sync.core.model;

import com.soybeany.sync.core.exception.SyncRequestException;
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
public class SyncDTO {

    private Boolean isNorm;
    private String dataJson;
    private String exception;
    private String errMsg;

    public static SyncDTO norm(Object obj) {
        return new SyncDTO(true, GSON.toJson(obj), null, null);
    }

    public static SyncDTO error(Throwable e) {
        byte[] bytes;
        try {
            bytes = SerializeUtils.serialize(e);
        } catch (IOException e2) {
            return SyncDTO.error("序列化异常信息失败:" + e2.getMessage());
        }
        return new SyncDTO(false, null, HexUtils.bytesToHex(bytes), null);
    }

    public static SyncDTO error(String errMsg) {
        return new SyncDTO(false, null, null, errMsg);
    }

    public <T> T getData(Type type) {
        return GSON.fromJson(dataJson, type);
    }

    public String getParsedErrMsg() {
        try {
            throwException();
            return null;
        } catch (Throwable t) {
            return t.getMessage();
        }
    }

    public <T> T throwException() throws Exception {
        if (null != exception) {
            Exception exception;
            try {
                exception = SerializeUtils.deserialize(HexUtils.hexToByteArray(this.exception));
            } catch (IOException | ClassNotFoundException e) {
                throw new SyncRequestException("反序列化异常信息失败:" + e.getMessage());
            }
            throw exception;
        } else if (null != errMsg) {
            throw new SyncRequestException(errMsg);
        }
        throw new SyncRequestException("没有具体的异常信息");
    }

}
