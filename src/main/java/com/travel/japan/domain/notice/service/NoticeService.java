package com.travel.japan.domain.notice.service;

import com.travel.japan.domain.notice.entity.Notice;
import com.travel.japan.domain.notice.entity.NoticeImage;
import com.travel.japan.global.exception.ResourceNotFoundException;
import com.travel.japan.domain.notice.repository.NoticeRepository;
import com.travel.japan.global.s3.S3Upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    private final S3Upload s3Upload;
    @Autowired
    public NoticeService(S3Upload s3Upload) {
        this.s3Upload = s3Upload;
    }
    // 게시글 생성
    public Notice createNotice(Notice notice, List<MultipartFile> files) {

        // 먼저 게시글을 저장 (ID가 할당됨)
        Notice savedNotice = noticeRepository.save(notice);
        if (files != null && !files.isEmpty()) {
            List<NoticeImage> noticeImages = files.stream()
                    .map(file -> {
                        try {
                            String imageUrl = s3Upload.uploadFiles(file, "images");  // S3에 업로드
                            return new NoticeImage(null, imageUrl, notice); // 업로드된 URL을 NoticeImage 객체에 설정
                        } catch (IOException e) {
                            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
                        }
                    })
                    .collect(Collectors.toList());
            notice.setBoardImages(noticeImages); // 이미지 리스트 설정
        }

        // 게시글 저장
        return noticeRepository.save(notice);
    }



    // 전체 게시글 조회
    public Page<Notice> listAllNotices(Pageable pagable) {
        return noticeRepository.findAll(pagable);
    }


    // 게시글 ID로 조회
    public Notice getBoardById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));
    }

    // 게시글 수정
    public Notice updateBoard(Long id, Notice boardDetails) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));

        notice.setTitle(boardDetails.getTitle());
        notice.setContent(boardDetails.getContent());

        return noticeRepository.save(notice);
    }

    // 게시글 삭제
    public Map<String, Boolean> deleteBoard(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));

        // S3에서 게시글에 연결된 이미지 삭제 처리
        for (NoticeImage image : notice.getBoardImages()) {
            String imageUrl = image.getUrl();
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);  // S3에 저장된 파일 이름 추출
            s3Upload.deleteFile(fileName, "images"); // S3에서 파일 삭제
        }

        noticeRepository.delete(notice);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}