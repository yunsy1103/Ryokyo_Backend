package com.travel.japan.domain.location.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
//사용자 위치 ( 경도,위도 )
public class LocationRequest {
    private double latitude;
    private double longitude;

}