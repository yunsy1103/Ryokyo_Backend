package com.travel.japan.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NoticeResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private List<String> imageUrls;  // S3에서 접근 가능한 이미지 URL 목록
}

