package com.nhnacademy.data4library.dto.naru.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.data4library.dto.naru.common.DocWrapped;

import java.util.List;

public record LoanItemResponse(
        @JsonProperty("resultNum")
        int resultNum,

        @JsonProperty("numFound")
        int numFound,

        @JsonProperty("docs")
        List<DocWrapped<LoanItem>> docs
) {
    public record LoanItem(
            @JsonProperty("no")
            int no,

            @JsonProperty("ranking")
            String ranking,

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

            @JsonProperty("bookImageURL")
            String bookImageUrl,

            @JsonProperty("loan_count")
            String loanCount
    ) {}
}