package com.nhnacademy.data4library.agent;

import com.nhnacademy.data4library.annotation.Agent;
import com.nhnacademy.data4library.api.NaruApiClient;
import lombok.RequiredArgsConstructor;

@Agent
@RequiredArgsConstructor
public class SearchLibrariesByBooksAgent {

    private final NaruApiClient naruApiClient;
}