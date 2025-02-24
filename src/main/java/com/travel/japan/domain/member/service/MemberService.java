package com.travel.japan.domain.member.service;

import com.travel.japan.domain.member.dto.MemberProfileUpdateDto;
import com.travel.japan.domain.member.dto.MemberSignInDto;
import com.travel.japan.domain.member.dto.MemberSignUpDto;
import com.travel.japan.domain.member.entity.Member;
import com.travel.japan.global.jwt.TokenInfo;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    public Long signup(MemberSignUpDto memberSignUpDto) throws Exception;

    public TokenInfo signIn(MemberSignInDto requestDto) throws Exception;

    public Member findByEmail(String email) ;
    public void updateProfileImage(String email, String imageUrl);

    public void updateProfile(String email, MemberProfileUpdateDto profileDto);

}


