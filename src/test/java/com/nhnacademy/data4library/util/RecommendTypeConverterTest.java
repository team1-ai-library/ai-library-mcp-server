package com.nhnacademy.data4library.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendTypeConverterTest {

    @Test
    void maniaTest_마니아() {
        assertThat(RecommendTypeConverter.convert("마니아")).isEqualTo("mania");
    }

    @Test
    void maniaTest_mania() {
        assertThat(RecommendTypeConverter.convert("mania")).isEqualTo("mania");
    }

    @Test
    void readerTest_다독자() {
        assertThat(RecommendTypeConverter.convert("다독자")).isEqualTo("reader");
    }

    @Test
    void readerTest_다독() {
        assertThat(RecommendTypeConverter.convert("다독")).isEqualTo("reader");
    }

    @Test
    void readerTest_reader() {
        assertThat(RecommendTypeConverter.convert("reader")).isEqualTo("reader");
    }

    @Test
    void defaultTest_mania() {
        assertThat(RecommendTypeConverter.convert(null)).isEqualTo("mania");
        assertThat(RecommendTypeConverter.convert(" ")).isEqualTo("mania");
        assertThat(RecommendTypeConverter.convert("testType")).isEqualTo("mania");
    }

}