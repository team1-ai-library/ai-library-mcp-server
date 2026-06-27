package com.nhnacademy.data4library.dto.naru.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.data4library.dto.naru.common.BookWrapped;

import java.util.List;

public record BookDetailResponse(
        @JsonProperty("detail")
        List<BookWrapped<BookDetail>> detail
) {
    public record BookDetail(
            @JsonProperty("no")
            int no,

            @JsonProperty("bookname")
            String bookName,

            @JsonProperty("authors")
            String authors,

            @JsonProperty("publisher")
            String publisher,

            @JsonProperty("publication_date")
            String publicationDate,

            @JsonProperty("publication_year")
            String publicationYear,

            @JsonProperty("isbn")
            String isbn,

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

            @JsonProperty("description")
            String description,

            @JsonProperty("bookImageURL")
            String bookImageUrl
    ) {}
}