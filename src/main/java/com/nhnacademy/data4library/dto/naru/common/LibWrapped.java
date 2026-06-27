package com.nhnacademy.data4library.dto.naru.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LibWrapped<T>(
        @JsonProperty("lib")
        T lib
) {
}