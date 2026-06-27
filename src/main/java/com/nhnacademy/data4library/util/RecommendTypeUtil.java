package com.nhnacademy.data4library.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RecommendTypeUtil {

    private static final Map<String, String> TYPE_MAP = new LinkedHashMap<>();

    // What the Fuck 마이아? 다독자?
    static {
        TYPE_MAP.put("마니아", "mania");
        TYPE_MAP.put("mania", "mania");

        TYPE_MAP.put("다독자", "reader");
        TYPE_MAP.put("다독", "reader");
        TYPE_MAP.put("reader", "reader");
    }

    public static String convert(String inputType) {
        if (Objects.isNull(inputType) || inputType.isBlank()) {
            return null;
        }
        return TYPE_MAP.get(inputType.trim()).toLowerCase();
    }
}
