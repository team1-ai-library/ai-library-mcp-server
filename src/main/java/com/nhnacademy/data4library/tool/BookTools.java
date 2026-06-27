package com.nhnacademy.data4library.tool;

import com.nhnacademy.data4library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookTools {

    private final BookService bookService;


}