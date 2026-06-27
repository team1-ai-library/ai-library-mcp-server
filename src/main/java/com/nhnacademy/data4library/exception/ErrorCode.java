package com.nhnacademy.data4library.exception;

public enum ErrorCode {

    // 내부 에러
    NOT_FOUND, // 조회 결과 없
    PARSE_ERROR, // JSON 파싱 실패
    INVALID_PARAM, // 잘못된 파라미터
    NOT_SUPPORTED, // 지원하지 않는 값 (지역, 타입 등)
    API_ERROR, // API 호출 실패

    // 도서관 정보나루 API 에러
    NARU_API_ERROR // 실제 정보나루가 보내는 errCode를 message(error)에 포함
}