package com.soupulsar.application.specialist.shared.summary;

public record SessionSummary(
        Long completed,
        Long confirmed
) {
    public long total() {
        return completed + confirmed;
    }
}