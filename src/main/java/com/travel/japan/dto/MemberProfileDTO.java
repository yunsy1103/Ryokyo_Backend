package com.travel.japan.dto;

import com.travel.japan.entity.Nationality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class MemberProfileDTO {
    private String email;
    private String nickname;
    private String gender;
    private LocalDate birth;
    private Nationality nationality;
}