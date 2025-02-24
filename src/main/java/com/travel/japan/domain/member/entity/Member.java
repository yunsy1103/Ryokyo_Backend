package com.travel.japan.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import com.travel.japan.global.common.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "nickname",nullable = true)
    private String nickname;

    @Column(name = "gender",nullable = true)
    private String gender;

    @Column(name = "birthday",nullable = true)
    private LocalDate birth;

    @Column(name = "nationality",nullable = true)
    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    //경도,위도 컬럼 추가
    @Builder.Default
    @Column(name = "latitude",nullable = true)
    private Double latitude = 0.0;

    @Builder.Default
    @Column(name = "longitude",nullable = true)
    private Double longitude = 0.0;

    // 프로필 이미지 URL 필드
    @Column(name = "profileImage",nullable = true)
    private String profileImageUrl;

    // 상태 필드 추가
    @Builder.Default
    @Column(name = "status",nullable = false)
    private String status = "active";  // 기본값을 'active'로 설정



    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}