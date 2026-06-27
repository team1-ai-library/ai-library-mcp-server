package com.nhnacademy.data4library.dto.response;

// libSrch, libSrchByBook 공통
public record LibraryInfo(
        String libCode,
        String libName,
        String address,
        String tel,
        String operatingTime,
        String homepage
) {
}