package com.travel.japan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Table(name = "filteredmember")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FilteredMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birthday", nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    //경도,위도 컬럼 추가
    @Column(name = "latitude",nullable = true)
    private Double latitude = 0.0;

    @Column(name = "longitude",nullable = true)
    private Double longitude = 0.0;

    // 프로필 이미지 URL 필드
    @Column(name = "profileImage",nullable = true)
    private String profileImageUrl;

    public FilteredMember(String email, String nickname, String gender, LocalDate birth, Nationality nationality, Double latitude, Double longitude,String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.nationality = nationality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImageUrl = profileImageUrl;
    }




}
