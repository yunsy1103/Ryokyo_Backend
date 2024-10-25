package com.travel.japan.controller;

import com.travel.japan.dto.LocationRequest;
import com.travel.japan.entity.Member;
import com.travel.japan.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveLocation(@RequestBody LocationRequest locationRequest) {
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        // 로그인된 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 이메일로 사용자를 조회하여 경도, 위도 업데이트
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setLatitude(latitude);
            member.setLongitude(longitude);
            memberRepository.save(member);

            return ResponseEntity.ok("Location data updated successfully for user: " + email);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }
    }
}
