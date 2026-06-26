package com.nhnacademy.data4library.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaruBook(
        @JsonProperty("bookname")
        String bookName,

        @JsonProperty("authors")
        String authors,

        @JsonProperty("publisher")
        String publisher,

        @JsonProperty("publication_year")
        String publicationYear,

        @JsonProperty("isbn13")
        String isbn13,

        @JsonProperty("addition_symbol")
        String additionSymbol,

        @JsonProperty("vol")
        String vol,

        @JsonProperty("class_no")
        String classNo,

        @JsonProperty("class_nm")
        String classNm,

        @JsonProperty("bookImageURL")
        String bookImageUrl,

        @JsonProperty("bookDtlUrl")
        String bookDtlUrl,

        @JsonProperty("loan_count")
        String loanCount
) {
}