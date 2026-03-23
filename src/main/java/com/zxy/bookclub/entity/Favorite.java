package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "favorite", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
    private Long userId;

    private LocalDateTime createTime = LocalDateTime.now();

    @Transient
    private Activity activity;
}