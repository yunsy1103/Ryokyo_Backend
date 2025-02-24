package com.travel.japan.domain.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.travel.japan.domain.gpt.entity.GptMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class GPTRequestDTO  implements Serializable {
    private String model;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    private Boolean stream;
    private List<GptMessage> messages;



    @Builder
    public GPTRequestDTO (String model, Integer maxTokens, Double temperature,
                          Boolean stream, List<GptMessage> messages
            /*,Double topP*/) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.messages = messages;
        //this.topP = topP;
    }
}
