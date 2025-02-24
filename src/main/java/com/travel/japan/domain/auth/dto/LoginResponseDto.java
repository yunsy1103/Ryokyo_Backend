package com.travel.japan.domain.auth.dto;

import com.travel.japan.global.jwt.TokenInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginResponseDto {
    private String message;
    private TokenInfo tokenInfo;

}