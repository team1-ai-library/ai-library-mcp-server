package com.nhnacademy.data4library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DateConverter {

    public DateConverter() {}

    private static final DateTimeFormatter API_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final List<DateTimeFormatter> ABSOLUTE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    );

    public static String parseAndFormat(String inputDate) {
        if (Objects.isNull(inputDate) || inputDate.isBlank()) {
            return null;
        }

        String date = inputDate.trim();

        return Optional.ofNullable(parseRelative(date))
                .or(() -> Optional.ofNullable(parseAbsolute(date)))
                .map(d -> d.format(API_FORMAT))
                .orElse(today());
    }

    /*
    @ToolParam(description = "날짜 (yyyy-MM-dd 형식 또는 오늘/내일/모레/글피/어제)") String date
     */
    private static LocalDate parseRelative(String input) {

        LocalDate today = LocalDate.now();

        return switch (input) {
            case "오늘" -> today;
            case "내일" -> today.plusDays(1);
            case "모레" -> today.plusDays(2);
            case "글피" -> today.plusDays(3);
            case "어제" -> today.minusDays(1);
            default -> null;
        };
    }

    private static LocalDate parseAbsolute(String inputDate) {

        for (DateTimeFormatter formatter : ABSOLUTE_FORMATTERS) {
            try {
                return LocalDate.parse(inputDate, formatter);
            } catch (DateTimeParseException ignored) {
                /* ignored */
            }
        }

        return null;
    }

    private static String today() {
        return LocalDate.now().format(API_FORMAT);
    }
}