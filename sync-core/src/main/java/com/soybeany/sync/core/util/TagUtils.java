package com.soybeany.sync.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public class TagUtils {

    private static final String SEPARATOR = "-";

    /**
     * 依据分隔符，将入参分隔
     */
    public static Map<String, Map<String, String[]>> split(Map<String, String[]> param) {
        Map<String, Map<String, String[]>> result = new HashMap<>();
        for (Map.Entry<String, String[]> entry : param.entrySet()) {
            String key = entry.getKey();
            int separatorIndex = key.indexOf(SEPARATOR);
            if (separatorIndex == -1) {
                continue;
            }
            String tag = key.substring(0, separatorIndex);
            String restKey = key.substring(separatorIndex + SEPARATOR.length());
            result.computeIfAbsent(tag, k -> new HashMap<>()).put(restKey, entry.getValue());
        }
        return result;
    }

    public static String addTag(String tag, String key) {
        return tag + SEPARATOR + key;
    }

}
