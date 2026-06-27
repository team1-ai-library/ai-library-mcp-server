package com.nhnacademy.data4library.dto.naru.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocWrapped<T>(
        @JsonProperty("doc")
        T doc
) {}