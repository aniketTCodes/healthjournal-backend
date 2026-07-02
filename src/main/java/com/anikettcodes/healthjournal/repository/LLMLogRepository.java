package com.anikettcodes.healthjournal.repository;

import com.anikettcodes.healthjournal.domain.LlmMacroLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LLMLogRepository extends JpaRepository<LlmMacroLog, Long> {

    LlmMacroLog save(LlmMacroLog llmMacroLog);
}
