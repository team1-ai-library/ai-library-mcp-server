package com.nhnacademy.data4library.dto.response;

// loanItemSrch 전용 -> "광주에서 2024년에 가장 많이 빌린 책 알려줘"
public record LoanBookResult(
        String isbn13,
        String bookName,
        String authors,
        String publisher,
        String loanCount
) {
}