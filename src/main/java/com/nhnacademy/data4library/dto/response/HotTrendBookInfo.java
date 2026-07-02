package com.nhnacademy.data4library.dto.response;

// hotTrend 전용
public record HotTrendBookInfo(
        String isbn13,
        String bookName,
        String authors,
        String publisher,
        int baseWeekRank
) {
}