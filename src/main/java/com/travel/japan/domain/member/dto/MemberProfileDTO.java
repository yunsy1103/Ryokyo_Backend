package com.travel.japan.domain.member.dto;

import com.travel.japan.domain.member.entity.Nationality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

//회원 프로필 조회
@Data
@Builder
@AllArgsConstructor
public class MemberProfileDTO {
    private String email;
    private String nickname;
    private String gender;
    private LocalDate birth;
    private Nationality nationality;
    private String profileImageUrl;
}