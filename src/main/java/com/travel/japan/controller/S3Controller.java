package com.travel.japan.controller;

import com.travel.japan.dto.ResponseDto;
import com.travel.japan.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Upload s3Upload;

    @PostMapping("/image")
    public ResponseDto imageUpload(@RequestPart(required = false) MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return new ResponseDto("파일이 유효하지 않습니다.");
        }

        try {
            String uploadUrl = s3Upload.uploadFiles(multipartFile, "static");
            return new ResponseDto(uploadUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}