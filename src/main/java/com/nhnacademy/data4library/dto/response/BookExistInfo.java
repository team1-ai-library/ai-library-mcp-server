package com.nhnacademy.data4library.dto.response;

// bookExist 전용
public record BookExistInfo(
        boolean hasBook,
        boolean loanAvailable
) {
}