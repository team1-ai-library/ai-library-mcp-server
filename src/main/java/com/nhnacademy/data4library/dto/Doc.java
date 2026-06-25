package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Doc(
        @JsonProperty("bookname")
        String bookName,

        @JsonProperty("authors")
        String authors,

        @JsonProperty("publisher")
        String publisher,

        @JsonProperty("publication_year")
        String publicationYear,

        @JsonProperty("isbn13")
        String isbn,

        @JsonProperty("addition_symbol")
        String additionSymbol,

        @JsonProperty("vol")
        String vol,

        @JsonProperty("class_no")
        String classNo,

        @JsonProperty("class_nm")
        String classNm,

        @JsonProperty("bookImageURL")
        String bookImageURL,

        @JsonProperty("bookDtlUrl")
        String bookDtlUrl,

        @JsonProperty("loan_count")
        String loanCount
) {
}