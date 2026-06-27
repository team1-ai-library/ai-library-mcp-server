package com.nhnacademy.data4library.dto.response;

// hotTrend 전용
public record HotTrendBookResult(
        String isbn13,
        String bookName,
        String authors,
        String publisher,
        int baseWeekRank
) {
}