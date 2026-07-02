package com.anikettcodes.healthjournal.dto;

public record LLMResponseDto(
        Long id,
        LlmFormatDto parsedResponse,
        String model,
        int latency
) {
}
