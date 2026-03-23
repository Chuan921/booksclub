package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long activityId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, length = 500)
    private String content;

    private Integer isRead = 0;

    private LocalDateTime createTime = LocalDateTime.now();

    @Transient
    private String activityTitle;
}