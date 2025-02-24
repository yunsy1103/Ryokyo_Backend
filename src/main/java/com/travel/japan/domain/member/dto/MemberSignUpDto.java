package com.travel.japan.domain.member.dto;

import com.travel.japan.domain.member.entity.Member;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Builder
@AllArgsConstructor
public class MemberSignUpDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String checkedPassword;


    public Member toEntity(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(password);
        return Member.builder()
                .email(email)
                .password(encodedPassword)
                .build();
    }
}