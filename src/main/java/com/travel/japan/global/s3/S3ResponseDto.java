package com.travel.japan.global.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//s3 전송 응답 dto
@Getter
@Setter
@AllArgsConstructor
public class S3ResponseDto {
    private String message;
}
