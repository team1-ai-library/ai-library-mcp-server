package com.nhnacademy.data4library.dto.naru.library;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookExistsResponse(
        @JsonProperty("result")
        BookExistResult result
) {
    public record BookExistResult(
            @JsonProperty("hasBook")
            String hasBook,

            @JsonProperty("loanAvailable")
            String loanAvailable
    ) {
    }
}