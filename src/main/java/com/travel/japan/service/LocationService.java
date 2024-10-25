package com.travel.japan.service;

import com.travel.japan.entity.FilteredMember;
import com.travel.japan.entity.Member;
import com.travel.japan.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private MemberRepository memberRepository;

    // 로그인된 사용자의 위치 정보 조회
    public Member getLoggedInUser(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // 반경 내 사용자 검색 및 저장
    public List<FilteredMember> getNearbyUsers(String email, double radius) {
        // 로그인된 사용자의 위치 정보 가져오기
        Member loggedInUser = getLoggedInUser(email);
         double lat1 = loggedInUser.getLatitude();
         double lon1 = loggedInUser.getLongitude();

        // 로그인된 사용자를 제외한 모든 사용자 가져오기
        List<Member> allMembers = memberRepository.findAllExceptEmail(email);

        List<FilteredMember> nearbyUsers = new ArrayList<>();

        // 반경 내 사용자 필터링
        for (Member member : allMembers) {
            double distance = calculateDistance(lat1, lon1, member.getLatitude(), member.getLongitude());

            // 거리가 반경(radius) 이내인 경우 추가
            if (distance <= radius) {
                FilteredMember filteredMember = new FilteredMember(member.getEmail(),member.getNickname(),member.getGender(), member.getBirth(), member.getNationality());
                nearbyUsers.add(filteredMember);
            }
        }
        // 리스트를 랜덤으로 섞음
        Collections.shuffle(nearbyUsers);

        // 최대 5명까지 반환
        return nearbyUsers.stream()
                .limit(5)  // 최대 5명까지 선택
                .collect(Collectors.toList());
    }

    // Haversine 공식을 사용하여 두 지점 간의 거리를 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구의 반경 (단위: km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;  // 두 지점 간의 거리 (km)
    }
}