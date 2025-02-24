package com.travel.japan.domain.notice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.travel.japan.global.common.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "notice")
@EqualsAndHashCode(callSuper = false)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(name = "nickname")
    private String nickname;

    @JsonProperty
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;


    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude  // 순환 참조 방지
    @JsonManagedReference
    @EqualsAndHashCode.Exclude  // 순환 참조 방지
    private List<NoticeImage> boardImages; // 게시글에 첨부된 이미지들

}
