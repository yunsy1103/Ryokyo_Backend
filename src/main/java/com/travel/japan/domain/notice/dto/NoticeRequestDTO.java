package com.travel.japan.domain.notice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class NoticeRequestDTO {

    private String nickname;

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 255, message = "Content cannot exceed 255 characters")
    private String content;

    private List<MultipartFile> images;; // 이미지 URL 리스트

    @Builder
    public NoticeRequestDTO(String nickname, String title, String content, List<MultipartFile> images) {
        this.nickname =nickname;
        this.title = title;
        this.content = content;
        this.images = images;
    }
}

