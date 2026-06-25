package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocWrapper(
        @JsonProperty("doc")
        Doc doc
) {
}