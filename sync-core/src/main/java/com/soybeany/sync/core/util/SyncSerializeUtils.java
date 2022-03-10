package com.soybeany.sync.core.util;

import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.model.SerializeType;
import com.soybeany.util.HexUtils;
import com.soybeany.util.SerializeUtils;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Type;
import java.util.Optional;

import static com.soybeany.sync.core.util.NetUtils.GSON;

/**
 * @author Soybeany
 * @date 2022/2/20
 */
public class SyncSerializeUtils {

    public static String toString(SerializeType type, Object obj, ISerializeHandler handler) throws Exception {
        if (null == obj) {
            return null;
        }
        switch (Optional.ofNullable(type).orElse(SerializeType.GSON)) {
            case GSON:
                return toJson(obj);
            case JAVA:
                return serialize(obj, handler);
            default:
                throw new SyncException("使用了不支持的序列化类型:" + type);
        }
    }

    public static <T> T fromString(SerializeType type, Type dataType, String data) throws ClassNotFoundException, IOException, SyncException {
        if (null == data) {
            return null;
        }
        switch (Optional.ofNullable(type).orElse(SerializeType.GSON)) {
            case GSON:
                return fromJson(data, dataType);
            case JAVA:
                return deserialize(data);
            default:
                throw new SyncException("使用了不支持的序列化类型:" + type);
        }
    }

    // ***********************内部方法****************************

    private static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    private static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    private static String serialize(Object obj, ISerializeHandler handler) throws Exception {
        try {
            return HexUtils.bytesToHex(SerializeUtils.serialize(obj));
        } catch (NotSerializableException e) {
            throw handler.onNotSerializable(e);
        }
    }

    private static <T> T deserialize(String hex) throws ClassNotFoundException, IOException {
        return SerializeUtils.deserialize(HexUtils.hexToByteArray(hex));
    }

    // ***********************内部类****************************

    public interface ISerializeHandler {
        Exception onNotSerializable(NotSerializableException e);
    }

}
