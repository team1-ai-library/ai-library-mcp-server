package com.nhnacademy.data4library.agent;

import com.nhnacademy.data4library.annotation.Agent;
import com.nhnacademy.data4library.api.NaruApiClient;
import lombok.RequiredArgsConstructor;

/**
 * searchHotBooksByDateAndRegion
 * searchHotTrendBooks
 */
@Agent
@RequiredArgsConstructor
public class SearchHotBooksAgent {

    private final NaruApiClient naruApiClient;
}
