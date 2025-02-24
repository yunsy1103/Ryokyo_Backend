package com.travel.japan.domain.gpt.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//GPT message
public class GptMessage {

    private String role;
    private String content;

}

