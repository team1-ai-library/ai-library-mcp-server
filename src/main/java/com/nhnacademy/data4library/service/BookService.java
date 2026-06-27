package com.nhnacademy.data4library.service;

import com.nhnacademy.data4library.api.NaruApiClient;
import com.nhnacademy.data4library.dto.response.BookDetailInfo;
import com.nhnacademy.data4library.dto.response.BookInfo;
import com.nhnacademy.data4library.dto.response.HotTrendBookInfo;
import com.nhnacademy.data4library.dto.response.LoanBookInfo;
import com.nhnacademy.data4library.exception.NaruApiException;
import com.nhnacademy.data4library.util.DateConverter;
import com.nhnacademy.data4library.util.RecommendTypeConverter;
import com.nhnacademy.data4library.util.RegionCodeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final NaruApiClient naruApiClient;

    public List<BookInfo> searchBooks(String title) {

        log.info("[BookService] 도서 검색 - title: {}", title);

        return naruApiClient.searchBooks(title)
                .docs()
                .stream()
                .map(item -> new BookInfo(
                        item.doc().isbn13(),
                        item.doc().bookName(),
                        item.doc().authors(),
                        item.doc().publisher(),
                        item.doc().publicationYear(),
                        item.doc().loanCount()
                ))
                .toList();
    }

    public BookDetailInfo searchBookDetail(String isbn13) {

        log.info("[BookService] 도서 상세 검색 - isbn13: {}", isbn13);

        return naruApiClient.searchBookDetail(isbn13)
                .detail()
                .stream()
                .findFirst()
                .map(item -> new BookDetailInfo(
                        item.book().isbn13(),
                        item.book().bookName(),
                        item.book().authors(),
                        item.book().publisher(),
                        item.book().publicationYear(),
                        item.book().description()
                ))
                .orElseThrow(() -> new NaruApiException("NOT_FOUND", "도서 상세 정보를 찾을 수 없습니다."));
    }

    public List<LoanBookInfo> searchHotBooks(String startDate, String endDate, String region) {

        log.info("[BookService] 인기 대출 도서 검색 - startDate: {}, endDate: {}, region: {}", startDate, endDate, region);

        return this.naruApiClient.searchHotBooksByDateAndRegion(
                        DateConverter.parseAndFormat(startDate),
                        DateConverter.parseAndFormat(endDate),
                        RegionCodeConverter.convert(region)
                )
                .docs()
                .stream()
                .map(item -> new LoanBookInfo(
                        item.doc().isbn13(),
                        item.doc().bookName(),
                        item.doc().authors(),
                        item.doc().publisher(),
                        item.doc().loanCount()
                ))
                .toList();
    }

    public List<HotTrendBookInfo> searchHotTrendBooks(String searchDate) {

        log.info("[BookService] 급상승 도서 검색 - searchDate: {}", searchDate);

        return naruApiClient.searchHotTrendBooks(DateConverter.parseAndFormat(searchDate))
                .results()
                .getFirst()
                .result()
                .docs()
                .stream()
                .map(item -> new HotTrendBookInfo(
                        item.doc().isbn13(),
                        item.doc().bookName(),
                        item.doc().authors(),
                        item.doc().publisher(),
                        item.doc().baseWeekRank()
                ))
                .toList();
    }

    public List<BookInfo> searchRecommendBooks(String isbn13, String type) {

        log.info("[BookService] 추천 도서 검색 - isbn13: {}, type: {}", isbn13, type);

        return this.naruApiClient.searchRecommendBooks(isbn13, RecommendTypeConverter.convert(type))
                .docs()
                .stream()
                .map(item -> new BookInfo(
                        item.book().isbn13(),
                        item.book().bookName(),
                        item.book().authors(),
                        item.book().publisher(),
                        item.book().publicationYear(),
                        null // recommandList에는 loanCount 없음
                ))
                .toList();
    }
}