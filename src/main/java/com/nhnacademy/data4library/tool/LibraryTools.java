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

    // "광주에 어떤 도서관이 있어?" 와 같은 도서관 정보 자체를 묻는 유스케이스에 대응
    @Tool(description = """
            ## 지역 도서관 검색
            지역명으로 해당 지역의 도서관 목록을 검색합니다.
            
            ### 반환 필드
            - `libCode`: 도서관 코드
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
            ## 도서 대출 가능 도서관 검색
            특정 도서를 소장하고 있는 도서관 목록과 대출 가능 여부를 한 번에 조회합니다.
            ISBN13과 지역명을 받아 해당 지역의 도서관별 소장 여부와 대출 가능 여부를 반환합니다.
            
            ### 반환 필드
            - `libCode`: 도서관 코드
            - `libName`: 도서관명
            - `address`: 주소
            - `tel`: 전화번호
            - `operatingTime`: 운영시간
            - `homepage`: 홈페이지 URL
            - `hasBook`: 소장 여부 (true/false)
            - `loanAvailable`: 대출 가능 여부 (true/false)
            
            ### 주의사항
            - ISBN13은 searchBooks로 먼저 조회하세요.
            - 도서관별 소장 여부와 대출 가능 여부를 한 번에 반환합니다.
            """)
    public List<LibraryAvailabilityInfo> searchAvailableLibraries(
            @ToolParam(description = "ISBN13 13자리 숫자 (예: 9788960777330) (searchBooks로 먼저 조회)") String isbn13,
            @ToolParam(description = "지역명 (예: 광주, 서울, 경기)") String region) {

        log.info("[LibraryTools] 대출 가능 도서관 검색 - isbn13: {}, region: {}", isbn13, region);

        return this.libraryService.searchAvailableLibraries(isbn13, region);
    }
}