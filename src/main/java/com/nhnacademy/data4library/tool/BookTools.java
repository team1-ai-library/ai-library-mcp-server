package com.nhnacademy.data4library.tool;

import com.nhnacademy.data4library.dto.response.BookInfo;
import com.nhnacademy.data4library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookTools {

    private final BookService bookService;

    @Tool(description = """
            도서관 시스템에서 도서를 제목으로 검색합니다.
            도서 목록과 ISBN을 반환합니다.
            """)
    public List<BookInfo> searchBooks(@ToolParam(description = "검색할 도서 제목") String title) {
        return bookService.searchBooks(title);
    }


}