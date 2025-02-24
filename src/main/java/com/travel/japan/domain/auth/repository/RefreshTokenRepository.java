package com.travel.japan.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.travel.japan.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    //  Optional<RefreshToken> findByStudentId(String studentId);
    Optional<RefreshToken> findByRefreshToken(String token);
    Optional<RefreshToken> findByEmail(String email);
}
