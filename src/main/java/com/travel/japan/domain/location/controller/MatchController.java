package com.travel.japan.domain.location.controller;

import com.travel.japan.domain.member.entity.FilteredMember;
import com.travel.japan.domain.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/match")
//사용자 매칭(국적,위치)하여 채팅을 시작
public class MatchController {

    private final LocationService locationService;

    @Autowired
    public MatchController(LocationService locationService) {
        this.locationService = locationService;
    }

    // 로그인된 사용자의 이메일을 기반으로 반경 5km 내의 다른 사용자 조회
    @GetMapping("/nearby")
    public List<FilteredMember> getNearbyUsers() {
        double radius = 5.0;  // 반경 5km

        // 로그인된 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return locationService.getNearbyUsers(email, radius);
    }

}