package com.nhnacademy.data4library.tool;

import com.nhnacademy.data4library.dto.response.BookExistInfo;
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
            검색된 도서관 목록을 반환하며, 각 도서관은 도서관 코드, 도서관명, 주소, 전화번호, 운영시간, 홈페이지를 포함합니다.
            """)
    public List<LibraryInfo> searchLibraries(@ToolParam(description = "지역명 (예: 광주, 서울, 경기)") String region) {

        log.info("[LibraryTools] 도서관 검색 - region: {}", region);
        return libraryService.searchLibraries(region);
    }

    @Tool(description = """
            특정 도서를 소장하고 있는 도서관 목록을 조회합니다.
            ISBN13과 지역명을 받아 해당 도서를 소장한 도서관 목록을 반환하며, 각 도서관은 도서관 코드, 도서관명, 주소, 전화번호, 운영시간, 홈페이지를 포함합니다.
            """)
    public List<LibraryInfo> searchLibrariesByBooks(@ToolParam(description = "ISBN13 13자리 (예: 9788960777330)") String isbn13,
                                                    @ToolParam(description = "지역명 (예: 광주, 서울, 경기)") String region) {

        log.info("[LibraryTools] 도서 소장 도서관 검색 - isbn: {}, region: {}", isbn13, region);
        return libraryService.searchLibrariesByBooks(isbn13, region);
    }

    @Tool(description = """
            특정 도서관에 특정 도서가 소장되어 있는지 확인합니다.
            도서관 코드와 ISBN13을 받아 소장 여부와 대출 가능 여부를 반환합니다.
            """)
    public BookExistInfo checkBookExists(@ToolParam(description = "도서관 코드 (예: 129003)") String libCode,
                                         @ToolParam(description = "ISBN13 13자리 (예: 9788960777330)") String isbn13
    ) {

        log.info("[LibraryTools] 도서 소장 여부 확인 - libCode: {}, isbn13: {}", libCode, isbn13);
        return libraryService.checkBookExists(libCode, isbn13);
    }
}