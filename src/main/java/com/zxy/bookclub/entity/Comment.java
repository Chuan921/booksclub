package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createTime = LocalDateTime.now();

    @Transient
    private String userName;
    @Transient
    private Integer likeCount;
    @Transient
    private Boolean liked;
}