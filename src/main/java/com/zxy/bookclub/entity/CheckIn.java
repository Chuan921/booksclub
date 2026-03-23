package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "check_in", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime checkInTime;
}