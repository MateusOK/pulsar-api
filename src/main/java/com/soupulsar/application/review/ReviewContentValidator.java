package com.soupulsar.application.review;

import java.util.List;
import java.util.regex.Pattern;

public class ReviewContentValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "(?i)\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b"
    );

    private static final Pattern CPF_FORMATTED_PATTERN = Pattern.compile(
            "(?<!\\d)(?:\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{9}-\\d{2})(?!\\d)"
    );

    private static final Pattern CPF_LABELED_PATTERN = Pattern.compile(
            "(?i)\\bCPF\\s*[:#-]?\\s*\\d[\\d.\\s-]{8,13}\\d\\b"
    );

    private static final Pattern ELEVEN_DIGITS_PATTERN = Pattern.compile(
            "(?<!\\d)\\d{11}(?!\\d)"
    );

    private static final Pattern CNPJ_PATTERN = Pattern.compile(
            "(?<!\\d)\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}(?!\\d)"
    );

    private static final Pattern PHONE_WITH_AREA_OR_COUNTRY_PATTERN = Pattern.compile(
            "(?:(?<!\\d)\\+\\d{1,3}[\\s.-]?)?(?:\\(\\d{2,3}\\)|(?<!\\d)\\d{2,3})[\\s.-]?\\d{4,5}[\\s.-]?\\d{4}(?!\\d)"
    );

    private static final Pattern LOCAL_PHONE_PATTERN = Pattern.compile(
            "(?<!\\d)(?:9\\d{4}|\\d{4})[-\\s]\\d{4}(?!\\d)"
    );

    private static final Pattern INTERNATIONAL_PHONE_PATTERN = Pattern.compile(
            "(?<!\\w)\\+\\d{1,3}(?:[\\s.-]?\\d{2,5}){2,4}(?!\\d)"
    );

    private static final Pattern RG_FORMATTED_PATTERN = Pattern.compile(
            "(?<!\\d)\\d{2}\\.\\d{3}\\.\\d{3}-[\\dXx](?!\\w)"
    );

    private static final Pattern RG_LABELED_PATTERN = Pattern.compile(
            "(?i)\\bRG\\s*[:#-]?\\s*\\d{5,9}[\\dXx]?\\b"
    );

    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile(
            "(?<!\\d)(?:\\d{4}[\\s-]?){3}\\d{4}(?!\\d)"
    );

    private static final Pattern SSN_PATTERN = Pattern.compile(
            "(?<!\\d)\\d{3}-\\d{2}-\\d{4}(?!\\d)"
    );

    private static final Pattern CEP_PATTERN = Pattern.compile(
            "(?<!\\d)\\d{5}-\\d{3}(?!\\d)"
    );

    private static final List<Pattern> SENSITIVE_PATTERNS = List.of(
            EMAIL_PATTERN,
            CPF_FORMATTED_PATTERN,
            CPF_LABELED_PATTERN,
            ELEVEN_DIGITS_PATTERN,
            CNPJ_PATTERN,
            PHONE_WITH_AREA_OR_COUNTRY_PATTERN,
            LOCAL_PHONE_PATTERN,
            INTERNATIONAL_PHONE_PATTERN,
            RG_FORMATTED_PATTERN,
            RG_LABELED_PATTERN,
            CREDIT_CARD_PATTERN,
            SSN_PATTERN,
            CEP_PATTERN
    );

    public void validate(String comment) {
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Review comment cannot be empty.");
        }

        for (Pattern pattern : SENSITIVE_PATTERNS) {
            if (pattern.matcher(comment).find()) {
                throw new IllegalArgumentException("Review comment cannot contain sensitive personal information.");
            }
        }
    }
}
