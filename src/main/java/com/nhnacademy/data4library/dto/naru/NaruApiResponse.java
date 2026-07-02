package com.nhnacademy.data4library.dto.naru;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaruApiResponse<T>(
        @JsonProperty("response")
        T response
) {
}