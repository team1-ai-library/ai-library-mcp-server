package com.nhnacademy.data4library.dto.response;

// srchDtlList 전용
public record BookDetailResult(
        String isbn13,
        String bookName,
        String authors,
        String publisher,
        String publicationYear,
        String description
) {
}