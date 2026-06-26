package com.nhnacademy.data4library.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookWrapped<T>(
        @JsonProperty("book")
        T book
) {
}