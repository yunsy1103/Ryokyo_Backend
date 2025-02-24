package com.travel.japan.domain.location.controller;

import com.travel.japan.domain.location.dto.LocationRequest;
import com.travel.japan.domain.member.entity.Member;
import com.travel.japan.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private MemberRepository memberRepository;

    @PutMapping("/save")
    public ResponseEntity<String> saveLocation(@RequestBody LocationRequest locationRequest) {
        System.out.println("saveLocation endpoint reached");
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        System.out.println("Authentication after setting: " + SecurityContextHolder.getContext().getAuthentication());

        // 이메일로 사용자를 조회하여 경도, 위도 업데이트
        Optional<Member> memberOptional = memberRepository.findByEmail(currentEmail);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setLatitude(latitude);
            member.setLongitude(longitude);
            memberRepository.save(member);

            return ResponseEntity.ok("Location data updated successfully for user: " + currentEmail);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }
    }
}
