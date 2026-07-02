package com.nhnacademy.data4library.tool;

import com.nhnacademy.data4library.dto.response.LibraryAvailabilityInfo;
import com.nhnacademy.data4library.dto.response.LibraryInfo;
import com.nhnacademy.data4library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryTools {

    private final LibraryService libraryService;

    @Tool(description = """
            지역명으로 도서관 목록을 검색합니다.
            "광주에 어떤 도서관이 있어?", "서울 도서관 알려줘" 처럼
            도서관 목록 자체가 궁금할 때만 사용하세요.
            특정 도서를 빌릴 수 있는 도서관을 찾을 때는 절대 사용하지 말고
            searchAvailableLibraries를 사용하세요.
            """)
    public List<LibraryInfo> searchLibraries(@ToolParam(description = "지역명 (예: 광주, 서울, 경기)") String region) {

        log.info("[LibraryTools] 도서관 검색 - region: {}", region);
        return libraryService.searchLibraries(region);
    }

    @Tool(description = """
            특정 도서를 대출할 수 있는 도서관을 검색합니다.
            "광주에서 코스모스 빌릴 수 있어?" 처럼 특정 도서의 대출 가능 여부가 궁금할 때 사용하세요.
            
            ### 반드시 지켜야 할 순서
            1. 반드시 searchBooks로 ISBN13을 먼저 조회하세요.
            2. 조회된 ISBN13으로 이 도구를 호출하세요.
            
            ### 절대 금지
            - ISBN13을 직접 만들거나 추측해서 넣지 마세요.
            - searchBooks 없이 이 도구를 먼저 호출하지 마세요.
            """)
    public List<LibraryAvailabilityInfo> searchAvailableLibraries(
            @ToolParam(description = "ISBN13 13자리 숫자 (searchBooks Tool로 먼저 조회)") String isbn13,
            @ToolParam(description = "지역명 (예: 광주, 서울, 경기)") String region) {

        log.info("[LibraryTools] 대출 가능 도서관 검색 - isbn13: {}, region: {}", isbn13, region);

        return this.libraryService.searchAvailableLibraries(isbn13, region);
    }
}