package com.travel.japan.domain.member.controller;

import com.travel.japan.domain.auth.dto.LoginResponseDto;
import com.travel.japan.domain.member.dto.MemberProfileDTO;
import com.travel.japan.domain.member.dto.MemberProfileUpdateDto;
import com.travel.japan.domain.member.dto.MemberSignInDto;
import com.travel.japan.domain.member.dto.MemberSignUpDto;
import com.travel.japan.domain.member.entity.Member;
import com.travel.japan.global.jwt.TokenInfo;
import com.travel.japan.domain.member.service.MemberService;
import com.travel.japan.global.s3.S3Upload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Member", description = "Member API")
public class MemberController {
    private final MemberService memberService;
    private final S3Upload s3Upload;


    @Operation(summary = "회원 등록", description = "회원 가입")
    @PostMapping("/register")
    public ResponseEntity<String> join(@Validated @RequestBody MemberSignUpDto register) {
        try {
            Long memberId = memberService.signup(register);
            return ResponseEntity.ok(memberId.toString()); // 상태 코드 200과 함께 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 중 오류가 발생했습니다: " + e.getMessage()); // 예외 발생 시 상태 코드 500과 메시지 반환
        }
    }

    @Operation(summary = "회원 로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberSignInDto request) {
        try {
            TokenInfo tokenInfo = memberService.signIn(request);
            LoginResponseDto response = LoginResponseDto.builder().message("로그인 성공").tokenInfo(tokenInfo).build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
    }


    @Operation(summary = "회원 페이지", description = "마이페이지")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody MemberProfileUpdateDto profileDto) {
        // 현재 인증된 사용자의 이메일을 SecurityContext에서 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 이메일을 가져옴

        // 서비스 계층으로 이메일과 프로필 정보를 전달하여 업데이트 수행
        memberService.updateProfile(currentEmail, profileDto);

        return ResponseEntity.ok("프로필 업데이트 성공");
    }

    @Operation(summary = "회원 페이지 이미지 추가", description = "마이페이지")
    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfileImage(@RequestPart("newProfileImage") MultipartFile newProfileImage)  {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();

            // 이미지 파일 업로드
            String imageUrl = s3Upload.uploadFiles(newProfileImage, "profile_images");

            // 서비스 계층을 통해 프로필 이미지 URL 업데이트
            memberService.updateProfileImage(currentEmail, imageUrl);

            return ResponseEntity.ok("프로필 이미지 업데이트 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드에 실패했습니다.");
        }
    }

    @Operation(summary = "회원 페이지 조회", description = "마이페이지 조회")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();

            Member member = memberService.findByEmail(currentEmail);

            if (member != null) {
                MemberProfileDTO profileDto = MemberProfileDTO.builder()
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .gender(member.getGender())
                        .birth(member.getBirth())
                        .nationality(member.getNationality())
                        .profileImageUrl(member.getProfileImageUrl())
                        .build();
                return ResponseEntity.ok(profileDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}