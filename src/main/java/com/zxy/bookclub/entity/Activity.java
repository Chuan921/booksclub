package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String type; // ONLINE, OFFLINE, HYBRID

    private String location;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private Integer maxParticipants = 0;

    @Column(nullable = false)
    private Long creatorId;

    private String status = "ACTIVE";

    private LocalDateTime createTime = LocalDateTime.now();

    @Transient
    private String creatorName;
    @Transient
    private Integer currentParticipants;
    @Transient
    private Integer likeCount;
    @Transient
    private Boolean liked;
    @Transient
    private Boolean favorited;
    @Transient
    private Boolean registered;
}