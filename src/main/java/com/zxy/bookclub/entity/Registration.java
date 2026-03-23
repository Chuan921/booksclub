package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "registration", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
    private Long userId;

    private LocalDateTime registerTime = LocalDateTime.now();

    private String status = "REGISTERED";

    @Transient
    private Activity activity;
    @Transient
    private Boolean checkedIn;
    @Transient
    private String userName;
}