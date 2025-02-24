package com.travel.japan.domain.member.service;

import com.travel.japan.domain.member.dto.MemberProfileUpdateDto;
import com.travel.japan.domain.member.dto.MemberSignInDto;
import com.travel.japan.domain.member.dto.MemberSignUpDto;
import com.travel.japan.domain.member.entity.Member;
import com.travel.japan.domain.auth.entity.RefreshToken;
import com.travel.japan.global.jwt.JwtTokenProvider;
import com.travel.japan.global.jwt.TokenInfo;
import com.travel.japan.domain.member.repository.MemberRepository;
import com.travel.japan.domain.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Lazy
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiredMs}")
    private Long expiredMs;

    @Override
    @Transactional
    public Long signup(MemberSignUpDto memberSignUptDto) throws Exception {
        if (memberRepository.findByEmail(memberSignUptDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (!memberSignUptDto.getPassword().equals(memberSignUptDto.getCheckedPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }


        Member member = memberSignUptDto.toEntity(passwordEncoder);
        memberRepository.save(member);

        return member.getId();
    }



    public TokenInfo signIn(MemberSignInDto requestDto){

        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(requestDto.getEmail(), Collections.singletonList("ROLE_USER"));

        //RefreshToken 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(requestDto.getEmail());

        //있으면 refreshToken 업데이트
        //없으면 새로 만들고 저장
        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenInfo.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenInfo.getRefreshToken(), requestDto.getEmail());
            refreshTokenRepository.save(newToken);
        }
        return tokenInfo;

    }

    public void updateProfile(String email, MemberProfileUpdateDto profileDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 프로필 정보 업데이트
        if (profileDto.getNickname() != null) {
            member.setNickname(profileDto.getNickname());
        }
        if (profileDto.getGender() != null) {
            member.setGender(profileDto.getGender());
        }
        if (profileDto.getBirth() != null) {
            member.setBirth(profileDto.getBirth());
        }
        if (profileDto.getNationality() != null) {
            member.setNationality(profileDto.getNationality());
        }
        if (profileDto.getProfileImageUrl() != null) {
            member.setProfileImageUrl(profileDto.getProfileImageUrl());
        }

        memberRepository.save(member);
    }

    public void updateProfileImage(String email, String imageUrl) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        member.setProfileImageUrl(imageUrl);
        memberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
    }
}



