package com.nhnacademy.data4library.tool;

import com.nhnacademy.data4library.dto.response.BookInfo;
import com.nhnacademy.data4library.dto.response.HotTrendBookInfo;
import com.nhnacademy.data4library.dto.response.LoanBookInfo;
import com.nhnacademy.data4library.exception.NaruApiException;
import com.nhnacademy.data4library.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookTools {

    private final BookService bookService;

    @Tool(description = """
            # 도서 제목 검색
            도서 제목의 키워드로 전국 도서 목록을 검색합니다.
            
            ## 반환 정보
            - ISBN13, 제목, 저자, 출판사, 출판년도, 대출 횟수
            
            ### 주의 사항
            - 이 도구는 title 파라미터만 받습니더. region 등 다른 파라미터는 절대 전달하지 마세요.
            - 지역별 도서관 검색은 searchLibraries를 사용하세요.
            - 특정 도서관의 소장 여부 확인은 checkBookExists를 사용하세요.
            - 반환된 ISBN13은 searchBookDetail, searchRecommendBooks, searchLibrariesByBooks에서 활용할 수 있습니다.
            
            """)
    public List<BookInfo> searchBooks(@ToolParam(description = "검색할 도서 제목 키워드 (예: 토비의 스프링, 파이썬)") String title) {

        log.info("[BookTools] 도서 검색 - title: {}", title);
        return bookService.searchBooks(title);
    }

    @Tool(description = """
            # 도서 상세 정보 조회
            
            ISBN13으로 특정 도서의 상세 정보를 조회합니다.
            
            ## 반환 정보
            - ISBN13, 제목, 저자, 출판사, 출판연도, 도서 설명
            
            ## 주의사항
            - ISBN13은 13자리 숫자입니다.
            - 도서 제목만 알고 있을 경우 searchBooks로 먼저 ISBN13을 조회한 후 사용하세요.
            - 도서를 찾을 수 없는 경우 오류 메시지를 반환합니다.
            """)
    public Object searchBookDetail(@ToolParam(description = "13자리 ISBN13 (예: 9788960777330)") String isbn13) {

        log.info("[BookTools] 도서 상세 조회 - isbn13: {}", isbn13);

        try {
            return bookService.searchBookDetail(isbn13);
        } catch (NaruApiException e) {
            log.warn("[BookTools] 도서 상세 조회 실패 - isbn13: {}, message: {}", isbn13, e.getMessage());
            return e.getMessage(); // "도서 상세 정보를 찾을 수 없습니다." 가 날라감
        }
    }

    @Tool(description = """
            # 인기 대출 도서 조회
            
            특정 기간과 지역에서 대출 횟수가 많은 인기 도서 목록을 조회합니다.
            
            ## 반환 정보
            - ISBN13, 제목, 저자, 출판사, 대출 횟수
            
            ## 파라미터 가이드
            - startDate, endDate: yyyy-MM-dd 형식 또는 자연어 표현 (오늘, 내일, 모레, 어제) 사용 가능
            - region: 지역명 (예: 광주, 서울, 경기도, 부산)
            
            ## 사용 예시
            - "올해 광주 인기 도서" → startDate=2026-01-01, endDate=2026-12-31, region=광주
            - "지난달 서울 인기 도서" → startDate=2026-05-01, endDate=2026-05-31, region=서울
            """)
    public List<LoanBookInfo> searchHotBook(@ToolParam(description = "조회 시작 날짜 (yyyy-MM-dd 형식 또는 오늘/내일/모레/어제)") String startDate,
                                            @ToolParam(description = "조회 종료 날짜 (yyyy-MM-dd 형식 또는 오늘/내일/모레/어제)") String endDate,
                                            @ToolParam(description = "지역명 (예: 광주, 서울, 경기도, 부산)") String region
    ) {

        log.info("[BookTools] 인기 대출 도서 조회 - startDate: {}, endDate: {}, region: {}", startDate, endDate, region);
        return bookService.searchHotBooks(startDate, endDate, region);
    }

    @Tool(description = """
            # 대출 급상승 도서 조회
            
            전주 대비 이번 주 대출 순위가 급상승한 도서 목록을 조회합니다.
            
            ## 반환 정보
            - ISBN13, 제목, 저자, 출판사, 이번 주 순위 (baseWeekRank)
            
            ## 파라미터 가이드
            - searchDate: 조회 기준 날짜로, 해당 날짜가 속한 주의 급상승 도서를 반환합니다.
            - yyyy-MM-dd 형식 또는 자연어 표현 (오늘, 내일, 모레, 어제) 사용 가능
            """)
    public List<HotTrendBookInfo> searchHotTrendBooks(@ToolParam(description = "조회 기준 날짜 (yyyy-MM-dd 형식 또는 오늘/내일/모레/어제)") String searchDate) {

        log.info("[BookTools] 급상승 도서 조회 - searchDate: {}", searchDate);
        return bookService.searchHotTrendBooks(searchDate);
    }

    @Tool(description = """
            ## 연관 도서 추천
            특정 도서를 읽은 독자들이 함께 읽은 도서 목록을 추천합니다.
            사용자가 읽은 도서 또는 관심 있는 도서의 ISBN13을 기준으로,
            비슷한 취향의 독자들이 읽은 도서를 추천받을 수 있습니다.
            
            ### 추천 타입
            - `마니아` / `mania`: 해당 도서를 여러 번 읽은 마니아 독자들의 추천
            - `다독자` / `reader`: 다양한 책을 많이 읽은 다독자들의 추천
            
            ### 반환 필드
            - `isbn13`: ISBN 13자리
            - `bookName`: 도서 제목
            - `authors`: 저자
            - `publisher`: 출판사
            - `publicationYear`: 출판연도
            
            ### 사용 예시
            - "JPA 프로그래밍과 비슷한 책 추천해줘" → searchBooks로 ISBN13 확보 후 mania 타입으로 호출
            - "다독자들이 추천하는 책 알려줘" → reader 타입으로 호출
            """)

    public List<BookInfo> searchRecommendBooks(@ToolParam(description = "기준 도서의 ISBN13 13자리 숫자 (예: 9788960777330)") String isbn13,
                                               @ToolParam(description = "추천 타입 (마니아/mania 또는 다독자/reader)") String type
    ) {

        log.info("[BookTools] 추천 도서 조회 - isbn13: {}, type: {}", isbn13, type);
        return bookService.searchRecommendBooks(isbn13, type);
    }
}