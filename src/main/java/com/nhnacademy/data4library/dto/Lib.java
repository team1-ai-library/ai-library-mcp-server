package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Lib(
        @JsonProperty("libCode")
        String libCode,

        @JsonProperty("libName")
        String libName,

        @JsonProperty("address")
        String address,

        @JsonProperty("tel")
        String tel,

        @JsonProperty("fax")
        String fax,

        @JsonProperty("latitude")
        String latitude,

        @JsonProperty("longitude")
        String longitude,

        @JsonProperty("homepage")
        String homepage,

        @JsonProperty("closed")
        String closed,

        @JsonProperty("operatingTime")
        String operatingTime,

        @JsonProperty("BookCount")
        String bookCount
) {
}