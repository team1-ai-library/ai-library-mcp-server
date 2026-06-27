package com.nhnacademy.data4library.tool;

import com.nhnacademy.data4library.dto.response.BookDetailInfo;
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
            도서관 시스템에서 도서를 제목으로 검색합니다.
            검색된 도서 목록을 반환하며, 각 도서는 ISBN13, 제목, 저자, 출판사, 출판연도, 대출 횟수를 포함합니다.
            """)
    public List<BookInfo> searchBooks(@ToolParam(description = "검색할 도서 제목") String title) {

        log.info("[BookTools] 도서 검색 - title: {}", title);
        return bookService.searchBooks(title);
    }

    @Tool(description = """
            ISBN13으로 도서 상세 정보를 조회합니다.
            ISBN13, 제목, 저자, 출판사, 출판연도, 도서 설명을 반환합니다.
            """)
    public Object searchBookDetail(@ToolParam(description = "ISBN13 13자리 (예: 9788960777330)") String isbn13) {

        log.info("[BookTools] 도서 상세 조회 - isbn13: {}", isbn13);

        try {
            return bookService.searchBookDetail(isbn13);
        } catch (NaruApiException e) {
            log.warn("[BookTools] 도서 상세 조회 실패 - isbn13: {}, message: {}", isbn13, e.getMessage());
            return e.getMessage(); // "도서 상세 정보를 찾을 수 없습니다." 가 날라감
        }
    }

    @Tool(description = """
            특정 기간, 특정 지역에서 인기 대출 도서 목록을 조회합니다.
            각 도서는 ISBN13, 제목, 저자, 출판사, 대출 횟수를 포함합니다.
            """)
    public List<LoanBookInfo> searchHotBook(@ToolParam(description = "시작 날짜 (yyyy-MM-dd 형식 또는 오늘/내일/모레/글피/어제)") String startDate,
                                            @ToolParam(description = "종료 날짜 (yyyy-MM-dd 형식 또는 오늘/내일/모레/글피/어제)") String endDate,
                                            @ToolParam(description = "지역명 (예: 광주, 서울)") String region
    ) {

        log.info("[BookTools] 인기 대출 도서 조회 - startDate: {}, endDate: {}, region: {}", startDate, endDate, region);
        return bookService.searchHotBooks(startDate, endDate, region);
    }

    @Tool(description = """
            대출 급상승 도서 목록을 조회합니다.
            전주 대비 이번 주 순위가 급상승한 도서 목록을 반환하며, 각 도서는 ISBN13, 제목, 저자, 출판사, 이번 주 순위를 포함합니다.
            """)
    public List<HotTrendBookInfo> searchHotTrendBooks(@ToolParam(description = "조회 날짜 (yyyy-MM-dd 형식 또는 오늘/내일/모레/글피/어제)") String searchDate) {

        log.info("[BookTools] 급상승 도서 조회 - searchDate: {}", searchDate);
        return bookService.searchHotTrendBooks(searchDate);
    }

    @Tool(description = """
            특정 도서의 ISBN13을 기준으로 추천 도서 목록을 조회합니다.
            마니아(mania) 또는 다독자(reader) 타입으로 추천 도서 목록을 반환하며, 각 도서는 ISBN13, 제목, 저자, 출판사, 출판연도를 포함합니다.
            """)
    public List<BookInfo> searchRecommendBooks(@ToolParam(description = "기준 도서의 ISBN13 13자리") String isbn13,
                                               @ToolParam(description = "추천 타입 (마니아/mania 또는 다독자/reader)") String type
    ) {

        log.info("[BookTools] 추천 도서 조회 - isbn13: {}, type: {}", isbn13, type);
        return bookService.searchRecommendBooks(isbn13, type);
    }
}