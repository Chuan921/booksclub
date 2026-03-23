-- 创建数据库
CREATE DATABASE IF NOT EXISTS bookclub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookclub;

-- 用户表
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    real_name VARCHAR(50),
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'USER',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 活动表
CREATE TABLE activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL COMMENT 'ONLINE-线上,OFFLINE-线下,HYBRID-混合',
    location VARCHAR(500),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    max_participants INT DEFAULT 0,
    creator_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES user(id)
);

-- 报名记录表
CREATE TABLE registration (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    register_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'REGISTERED',
    UNIQUE KEY uk_activity_user (activity_id, user_id),
    FOREIGN KEY (activity_id) REFERENCES activity(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 签到记录表
CREATE TABLE check_in (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    check_in_time DATETIME NOT NULL,
    UNIQUE KEY uk_checkin_activity_user (activity_id, user_id),
    FOREIGN KEY (activity_id) REFERENCES activity(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 评论表
CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (activity_id) REFERENCES activity(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 活动点赞表
CREATE TABLE activity_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_like_activity_user (activity_id, user_id),
    FOREIGN KEY (activity_id) REFERENCES activity(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 评论点赞表
CREATE TABLE comment_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_like_comment_user (comment_id, user_id),
    FOREIGN KEY (comment_id) REFERENCES comment(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 收藏表
CREATE TABLE favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_fav_activity_user (activity_id, user_id),
    FOREIGN KEY (activity_id) REFERENCES activity(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 站内消息表
CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    activity_id BIGINT,
    type VARCHAR(50) NOT NULL,
    content VARCHAR(500) NOT NULL,
    is_read TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (activity_id) REFERENCES activity(id)
);

-- 插入管理员账户
INSERT INTO user (username, password, nickname, real_name, phone, role) 
VALUES ('admin', 'admin123', '管理员', '系统管理员', '13800000000', 'ADMIN');

-- 插入测试用户
INSERT INTO user (username, password, nickname, real_name, phone, role) 
VALUES ('user1', '123456', '书友小明', '张明', '13811111111', 'USER');

-- 插入测试活动
INSERT INTO activity (title, description, type, location, start_time, end_time, max_participants, creator_id, status)
VALUES ('《深度工作》线上读书分享会', '一起探讨如何在碎片化时代保持专注，提升工作效率。', 'ONLINE', '腾讯会议', DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY) + INTERVAL 2 HOUR, 50, 1, 'ACTIVE');

INSERT INTO activity (title, description, type, location, start_time, end_time, max_participants, creator_id, status)
VALUES ('《人类简史》线下读书会', '面对面交流，分享阅读心得。', 'OFFLINE', '北京市朝阳区图书馆三楼会议室', DATE_ADD(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY) + INTERVAL 3 HOUR, 30, 1, 'ACTIVE');