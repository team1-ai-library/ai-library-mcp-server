package com.nhnacademy.data4library.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class DateConverterTest {

    private static final LocalDate FIXED_DATE = LocalDate.of(2026, Month.JUNE, 30);

    @Test
    void parseAndFormat_0() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(FIXED_DATE);
            assertThat(DateConverter.parseAndFormat("오늘")).isEqualTo("2026-06-30");
        }
    }

    @Test
    void parseAndFormat_1() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(FIXED_DATE);
            assertThat(DateConverter.parseAndFormat("내일")).isEqualTo("2026-07-01");
        }
    }

    @Test
    void parseAndFormat_2() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(FIXED_DATE);
            assertThat(DateConverter.parseAndFormat("모레")).isEqualTo("2026-07-02");
        }
    }

    @Test
    void parseAndFormat_3() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(FIXED_DATE);
            assertThat(DateConverter.parseAndFormat("글피")).isEqualTo("2026-07-03");
        }
    }

    @Test
    void parseAndFormat_4() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(FIXED_DATE);
            assertThat(DateConverter.parseAndFormat("어제")).isEqualTo("2026-06-29");
        }
    }

    @Test
    void parseAndFormat_nullOrBlank() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(FIXED_DATE);
            assertThat(DateConverter.parseAndFormat(null)).isNull();
            assertThat(DateConverter.parseAndFormat(" ")).isNull();
        }
    }

    @Test
    void dateFormat() {
        assertThat(DateConverter.parseAndFormat("2026-06-30")).isEqualTo("2026-06-30");
        assertThat(DateConverter.parseAndFormat("20260630")).isEqualTo("2026-06-30");
        assertThat(DateConverter.parseAndFormat("2026/06/30")).isEqualTo("2026-06-30");
    }

    @Test
    void cannotCheckType() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(FIXED_DATE);
            assertThat(DateConverter.parseAndFormat("testDate")).isEqualTo("2026-06-30");
        }
    }
}