package com.nhnacademy.data4library.util;

import com.nhnacademy.data4library.exception.NotSupportedRegionException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RegionCodeConverter {

    private RegionCodeConverter() {}

    private static final Map<String, String> REGION_MAP = new LinkedHashMap<>();

    static {
        REGION_MAP.put("서울", "11");
        REGION_MAP.put("서울특별시", "11");
        REGION_MAP.put("부산", "21");
        REGION_MAP.put("부산광역시", "21");
        REGION_MAP.put("대구", "22");
        REGION_MAP.put("대구광역시", "22");
        REGION_MAP.put("인천", "23");
        REGION_MAP.put("인천광역시", "23");
        REGION_MAP.put("광주", "24");
        REGION_MAP.put("광주광역시", "24");
        REGION_MAP.put("대전", "25");
        REGION_MAP.put("대전광역시", "25");
        REGION_MAP.put("울산", "26");
        REGION_MAP.put("울산광역시", "26");
        REGION_MAP.put("세종", "29");
        REGION_MAP.put("세종특별자치시", "29");
        REGION_MAP.put("경기", "31");
        REGION_MAP.put("경기도", "31");
        REGION_MAP.put("강원", "32");
        REGION_MAP.put("강원특별자치도", "32");
        REGION_MAP.put("충북", "33");
        REGION_MAP.put("충청북도", "33");
        REGION_MAP.put("충남", "34");
        REGION_MAP.put("충청남도", "34");
        REGION_MAP.put("전북", "35");
        REGION_MAP.put("전북특별자치도", "35");
        REGION_MAP.put("전남", "36");
        REGION_MAP.put("전라남도", "36");
        REGION_MAP.put("경북", "37");
        REGION_MAP.put("경상북도", "37");
        REGION_MAP.put("경남", "38");
        REGION_MAP.put("경상남도", "38");
        REGION_MAP.put("제주", "39");
        REGION_MAP.put("제주특별자치도", "39");
    }

    public static String convert(String regionName) {
        if (Objects.isNull(regionName) || regionName.isBlank()) {
            throw new NotSupportedRegionException("지역명이 비어있습니다.");
        }

        String result = REGION_MAP.get(regionName.trim());

        if (Objects.isNull(result)) {
            throw new NotSupportedRegionException("지원하지 않는 지역명입니다: " + regionName);
        }

        return result;
    }
}