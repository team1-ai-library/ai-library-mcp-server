package com.nhnacademy.data4library.dto.naru.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookWrapped<T>(
        @JsonProperty("book")
        T book
) {
}