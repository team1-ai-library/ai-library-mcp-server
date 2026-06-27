package com.nhnacademy.data4library.dto.response;

// srchBooks, recommandList 공통
public record BookInfo(
        String isbn13,
        String bookName,
        String authors,
        String publisher,
        String publicationYear,
        String loanCount
) {
}