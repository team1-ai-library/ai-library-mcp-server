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
        ## 지역 도서관 검색
        지역명으로 해당 지역의 도서관 목록을 검색합니다.
        
        ### 반환 필드
        - `libCode`: 도서관 코드 (checkBookExists 호출 시 사용)
        - `libName`: 도서관명
        - `address`: 주소
        - `tel`: 전화번호
        - `operatingTime`: 운영시간
        - `homepage`: 홈페이지 URL
        """)
    public List<LibraryInfo> searchLibraries(@ToolParam(description = "지역명 (예: 광주, 서울, 경기)") String region) {

        log.info("[LibraryTools] 도서관 검색 - region: {}", region);
        return libraryService.searchLibraries(region);
    }

    @Tool(description = """
        ## 도서 소장 도서관 검색
        특정 도서(ISBN13)를 소장하고 있는 도서관 목록을 지역별로 조회합니다.
        ISBN13은 searchBooks로 먼저 조회하세요.
        
        ### 반환 필드
        - `libCode`: 도서관 코드 (checkBookExists 호출 시 사용)
        - `libName`: 도서관명
        - `address`: 주소
        - `tel`: 전화번호
        - `operatingTime`: 운영시간
        - `homepage`: 홈페이지 URL
        """)
    public List<LibraryInfo> searchLibrariesByBooks(@ToolParam(description = "ISBN13 13자리 (예: 9788960777330)") String isbn13,
                                                    @ToolParam(description = "지역명 (예: 광주, 서울, 경기)") String region) {

        log.info("[LibraryTools] 도서 소장 도서관 검색 - isbn: {}, region: {}", isbn13, region);
        return libraryService.searchLibrariesByBooks(isbn13, region);
    }

    @Tool(description = """
        ## 도서관 소장 여부 확인
        특정 도서관에 특정 도서가 소장되어 있는지, 그리고 대출 가능한지 확인합니다.
        도서관 코드는 searchLibraries 또는 searchLibrariesByBooks로 먼저 조회해야 합니다.
        
        ### 반환 필드
        - `hasBook`: 소장 여부 (true/false)
        - `loanAvailable`: 대출 가능 여부 (true/false)
        """)
    public BookExistInfo checkBookExists(@ToolParam(description = "도서관 코드 (searchLibraries로 조회 가능) (예: 129003)") String libCode,
                                         @ToolParam(description = "ISBN13 13자리 (예: 9788960777330)") String isbn13
    ) {

        log.info("[LibraryTools] 도서 소장 여부 확인 - libCode: {}, isbn13: {}", libCode, isbn13);
        return libraryService.checkBookExists(libCode, isbn13);
    }
}