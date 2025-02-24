package com.travel.japan.domain.member.dto;

import com.travel.japan.domain.member.entity.Nationality;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

//마이페이지 프로필 정보 추가
@Data
@Builder
@AllArgsConstructor
public class MemberProfileUpdateDto {

    @Size(min=2, message = "닉네임이 너무 짧습니다.")

    private String nickname;       // null 허용

    private String gender;         // null 허용

    private LocalDate birth;       // null 허용

    private Nationality nationality; // null 허용

    private String profileImageUrl; // 현재 프로필 이미지 URL

    private MultipartFile newProfileImage; // 새로운 이미지를 업로드하기 위한 필드

    public MemberProfileUpdateDto() {
    }
}
