package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookExistResult(
        @JsonProperty("hasBook")
        String hasBook,

        @JsonProperty("loanAvailable")
        String loanAvailable
) {
}