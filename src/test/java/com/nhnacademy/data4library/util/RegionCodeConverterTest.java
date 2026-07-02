package com.nhnacademy.data4library.util;

import com.nhnacademy.data4library.exception.NaruApiException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RegionCodeConverterTest {

    @Test
    void regionTest() {
        assertAll(
            () -> assertThat(RegionCodeConverter.convert("서울")).isEqualTo("11"),
            () -> assertThat(RegionCodeConverter.convert("서울특별시")).isEqualTo("11"),
            () -> assertThat(RegionCodeConverter.convert("부산")).isEqualTo("21"),
            () -> assertThat(RegionCodeConverter.convert("부산광역시")).isEqualTo("21"),
            () -> assertThat(RegionCodeConverter.convert("대구")).isEqualTo("22"),
            () -> assertThat(RegionCodeConverter.convert("대구광역시")).isEqualTo("22"),
            () -> assertThat(RegionCodeConverter.convert("인천")).isEqualTo("23"),
            () -> assertThat(RegionCodeConverter.convert("인천광역시")).isEqualTo("23"),
            () -> assertThat(RegionCodeConverter.convert("광주")).isEqualTo("24"),
            () -> assertThat(RegionCodeConverter.convert("광주광역시")).isEqualTo("24"),
            () -> assertThat(RegionCodeConverter.convert("대전")).isEqualTo("25"),
            () -> assertThat(RegionCodeConverter.convert("대전광역시")).isEqualTo("25"),
            () -> assertThat(RegionCodeConverter.convert("울산")).isEqualTo("26"),
            () -> assertThat(RegionCodeConverter.convert("울산광역시")).isEqualTo("26"),
            () -> assertThat(RegionCodeConverter.convert("세종")).isEqualTo("29"),
            () -> assertThat(RegionCodeConverter.convert("세종특별자치시")).isEqualTo("29"),
            () -> assertThat(RegionCodeConverter.convert("경기")).isEqualTo("31"),
            () -> assertThat(RegionCodeConverter.convert("경기도")).isEqualTo("31"),
            () -> assertThat(RegionCodeConverter.convert("강원")).isEqualTo("32"),
            () -> assertThat(RegionCodeConverter.convert("강원특별자치도")).isEqualTo("32"),
            () -> assertThat(RegionCodeConverter.convert("충북")).isEqualTo("33"),
            () -> assertThat(RegionCodeConverter.convert("충청북도")).isEqualTo("33"),
            () -> assertThat(RegionCodeConverter.convert("충남")).isEqualTo("34"),
            () -> assertThat(RegionCodeConverter.convert("충청남도")).isEqualTo("34"),
            () -> assertThat(RegionCodeConverter.convert("전북")).isEqualTo("35"),
            () -> assertThat(RegionCodeConverter.convert("전북특별자치도")).isEqualTo("35"),
            () -> assertThat(RegionCodeConverter.convert("전남")).isEqualTo("36"),
            () -> assertThat(RegionCodeConverter.convert("전라남도")).isEqualTo("36"),
            () -> assertThat(RegionCodeConverter.convert("경북")).isEqualTo("37"),
            () -> assertThat(RegionCodeConverter.convert("경상북도")).isEqualTo("37"),
            () -> assertThat(RegionCodeConverter.convert("경남")).isEqualTo("38"),
            () -> assertThat(RegionCodeConverter.convert("경상남도")).isEqualTo("38"),
            () -> assertThat(RegionCodeConverter.convert("제주")).isEqualTo("39"),
            () -> assertThat(RegionCodeConverter.convert("제주특별자치도")).isEqualTo("39")
        );
    }

    @Test
    void regionNullOrBlankOrNotAllowRegionTest() {
        assertThatThrownBy(() -> RegionCodeConverter.convert(null))
                .isInstanceOf(NaruApiException.class)
                .hasMessage("지역명이 비어있습니다.");
        assertThatThrownBy(() -> RegionCodeConverter.convert(" "))
                .isInstanceOf(NaruApiException.class)
                .hasMessage("지역명이 비어있습니다.");
        assertThatThrownBy(() -> RegionCodeConverter.convert("뉴욕"))
                .isInstanceOf(NaruApiException.class)
                .hasMessage("지원하지 않는 지역명입니다: 뉴욕");
    }
}