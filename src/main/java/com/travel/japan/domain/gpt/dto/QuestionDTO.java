package com.travel.japan.domain.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
//Front단에서 요청하는 DTO
public class QuestionDTO implements Serializable {
    private String question;
}
