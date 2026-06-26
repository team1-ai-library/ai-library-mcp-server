package com.nhnacademy.data4library.dto.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.data4library.dto.common.LibWrapped;
import com.nhnacademy.data4library.dto.common.NaruLibrary;

import java.util.List;

public record LibrarySearchResponse(
        @JsonProperty("numFound")
        int numFound,

        @JsonProperty("resultNum")
        int resultNum,

        @JsonProperty("libs")
        List<LibWrapped<NaruLibrary>> libs
) {
}