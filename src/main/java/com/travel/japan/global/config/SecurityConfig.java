package com.travel.japan.global.config;

import com.travel.japan.global.jwt.JwtAuthenticationFilter;
import com.travel.japan.global.jwt.JwtFilter;
import com.travel.japan.global.jwt.JwtTokenProvider;
import com.travel.japan.domain.auth.repository.RefreshTokenRepository;
import com.travel.japan.domain.member.service.MemberService;
import com.travel.japan.domain.member.service.MemberServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private MemberService memberService;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Autowired
    @Lazy
    public void setMemberService(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }


    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(memberService, secretKey); // JwtFilter 객체 생성
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {} ) // CORS 활성화
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/match/nearby").authenticated()
                                .requestMatchers("/api/location/save").authenticated()
                                .requestMatchers("/firebase/**").authenticated()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/api/notice/**").authenticated()
                        .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/profile/**").authenticated()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/s3/image").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/gpt/**").permitAll() // GPT API 경로 허용
                        //.anyRequest().authenticated() // 다른 모든 요청은 인증 필요
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, refreshTokenRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JwtFilter 추가
                .build();
    }

}