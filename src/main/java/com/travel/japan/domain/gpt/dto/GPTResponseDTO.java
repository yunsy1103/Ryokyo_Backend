package com.travel.japan.domain.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.travel.japan.domain.gpt.entity.GptMessage;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
//ChatGPT 답변을 담을 DTO
public class GPTResponseDTO {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }

    @Getter
    @Setter
    public static class Choice {
        private GptMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;
        private int index;
    }
}