package com.soybeany.sync.core.model;

import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.util.HexUtils;
import com.soybeany.util.SerializeUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.NotSerializableException;

/**
 * @author Soybeany
 * @date 2021/11/3
 */
@Data
@RequiredArgsConstructor
public class SyncDTO {

    private final Boolean isNorm;
    private final String dataHex;
    private final String exception;
    private final String errMsg;

    public static SyncDTO norm(Object obj, IExceptionHandler handler) throws Exception {
        try {
            String data = null != obj ? HexUtils.bytesToHex(SerializeUtils.serialize(obj)) : null;
            return new SyncDTO(true, data, null, null);
        } catch (NotSerializableException e) {
            throw handler.onNotSerializable(e);
        }
    }

    public static SyncDTO error(Throwable e) {
        try {
            String exception = HexUtils.bytesToHex(SerializeUtils.serialize(e));
            return new SyncDTO(false, null, exception, null);
        } catch (IOException e2) {
            return SyncDTO.error("序列化异常信息失败:" + e2.getMessage() + "(" + e2.getClass() + ")");
        }
    }

    public static SyncDTO error(String errMsg) {
        return new SyncDTO(false, null, null, errMsg);
    }

    public <T> T toData() throws ClassNotFoundException, IOException {
        return null != dataHex ? SerializeUtils.deserialize(HexUtils.hexToByteArray(dataHex)) : null;
    }

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

    // ***********************内部类****************************

    public interface IExceptionHandler {
        Exception onNotSerializable(NotSerializableException e);
    }

}
