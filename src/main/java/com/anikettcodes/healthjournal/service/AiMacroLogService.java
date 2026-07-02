package com.anikettcodes.healthjournal.service;
import com.anikettcodes.healthjournal.domain.LlmMacroLog;
import com.anikettcodes.healthjournal.dto.LLMResponseDto;
import com.anikettcodes.healthjournal.dto.LlmFormatDto;
import com.anikettcodes.healthjournal.exception.LLMFormatException;
import com.anikettcodes.healthjournal.repository.LLMLogRepository;
import com.anikettcodes.healthjournal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiMacroLogService {

    private final ChatClient chatClient;
    private final LLMLogRepository llmLogRepository;
    private final UserRepository userRepository;

    private static final Duration LLM_TIMEOUT = Duration.ofSeconds(20);
    private static final String MODEL_NAME = "Qwen2.5:3b";

    public LLMResponseDto getMacroLog(String userInput, Long userId) {
        BeanOutputConverter<LlmFormatDto> converter = new BeanOutputConverter<>(LlmFormatDto.class);

        long startTime = System.nanoTime();
        ChatResponse response = callLlm(userInput, converter);
        long latencyMs = elapsedMs(startTime);

        validateResponse(response);

        Generation raw = response.getResult();
        log.info("Raw response: {}", raw);

        LlmFormatDto parsed = parseResponse(raw, converter);
        LlmMacroLog savedLog = persistLog(userId,userInput, raw, parsed, latencyMs);

        return new LLMResponseDto(
                savedLog.getId(),
                parsed,
                MODEL_NAME,
                savedLog.getLatency()
        );
    }

    private ChatResponse callLlm(String userInput, BeanOutputConverter<LlmFormatDto> converter) {
        try {
            return Mono.fromCallable(() ->
                            chatClient.prompt()
                                    .system(spec -> spec.text(buildSystemPrompt())
                                            .param("format", converter.getFormat()))
                                    .user(userInput)
                                    .call()
                                    .chatResponse()
                    )
                    .timeout(LLM_TIMEOUT)
                    .block();
        } catch (Exception e) {
            log.error("LLM call failed or timed out", e);
            throw new LLMFormatException("LLM did not respond in time");
        }
    }

    private String buildSystemPrompt() {
        return """
                "You are nutritionist and you give your best estimates of nutritional macro
                 confidence score between 0.0 to 1.0 and
                 error message if any which tell user what more they should answer if any in JSON format only format - {format}
                """;
    }

    private void validateResponse(ChatResponse response) {
        if (response == null || response.getResult() == null) {
            throw new LLMFormatException("No response received from LLM");
        }
    }

    private LlmFormatDto parseResponse(Generation raw, BeanOutputConverter<LlmFormatDto> converter) {
        try {
            return converter.convert(raw.getOutput().getText());
        } catch (Exception e) {
            log.error("Parse failure. Raw: {}", raw, e);
            throw new LLMFormatException("Error parsing LLM response");
        }
    }

    private LlmMacroLog persistLog(Long userId, String userInput, Generation raw, LlmFormatDto parsed, long latencyMs) {
        LlmMacroLog llmMacroLog = LlmMacroLog.builder()
                .user(userRepository.getReferenceById(userId))
                .userInput(userInput)
                .llmResponse(raw.getOutput().getText())
                .model(MODEL_NAME)
                .parsedCalories(parsed.parsedCalories())
                .parsedProtein(parsed.parsedProtein())
                .parsedCarbs(parsed.parsedCarbs())
                .parsedFat(parsed.parsedFat())
                .status("PENDING")
                .latency((int)(latencyMs))
                .build();

        return llmLogRepository.save(llmMacroLog);
    }

    private long elapsedMs(long startTimeNanos) {
        return (System.nanoTime() - startTimeNanos) / 1_000_000;
    }
}
