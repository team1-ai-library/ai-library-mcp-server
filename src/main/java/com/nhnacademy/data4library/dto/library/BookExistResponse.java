package com.nhnacademy.data4library.dto.library;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookExistResponse(
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