package com.nhnacademy.data4library.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record HotTrendResponse(
        @JsonProperty("results")
        List<ResultWrapper> results
) {
    public record ResultWrapper(
            @JsonProperty("result")
            Result result
    ) {}

    public record Result(
            @JsonProperty("date")
            String date,

            @JsonProperty("docs")
            List<HotTrendItemWrapper> docs
    ) {}

    public record HotTrendItemWrapper(
            @JsonProperty("doc")
            HotTrendItem doc
    ) {}

    public record HotTrendItem(
            @JsonProperty("no")
            int no,

            @JsonProperty("difference")
            int difference,

            @JsonProperty("baseWeekRank")
            int baseWeekRank,

            @JsonProperty("pastWeekRank")
            int pastWeekRank,

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