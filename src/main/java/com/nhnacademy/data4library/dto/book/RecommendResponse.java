package com.nhnacademy.data4library.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.data4library.dto.common.BookWrapped;

import java.util.List;

public record RecommendResponse(
        @JsonProperty("resultNum")
        int resultNum,

        @JsonProperty("docs")
        List<BookWrapped<RecommendItem>> docs
) {
    public record RecommendItem(
            @JsonProperty("no")
            int no,

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
            String bookImageUrl
    ) {}
}