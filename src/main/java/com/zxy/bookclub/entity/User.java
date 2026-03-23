package com.zxy.bookclub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nickname;
    private String realName;
    private String phone;

    @Column(nullable = false)
    private String role = "USER";

    private LocalDateTime createTime = LocalDateTime.now();
}