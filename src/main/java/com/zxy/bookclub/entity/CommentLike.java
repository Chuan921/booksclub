package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment_like", uniqueConstraints = @UniqueConstraint(columnNames = {"commentId", "userId"}))
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private Long userId;

    private LocalDateTime createTime = LocalDateTime.now();
}