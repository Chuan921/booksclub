package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity_like", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
public class ActivityLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
    private Long userId;

    private LocalDateTime createTime = LocalDateTime.now();
}