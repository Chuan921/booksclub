package com.zxy.bookclub.service;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Notification;
import com.zxy.bookclub.repository.ActivityRepository;
import com.zxy.bookclub.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ActivityRepository activityRepository;

    public void create(Long userId, Long activityId, String type, String content) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setActivityId(activityId);
        n.setType(type);
        n.setContent(content);
        n.setIsRead(0);
        n.setCreateTime(LocalDateTime.now());
        notificationRepository.save(n);
    }

    public Result<List<Notification>> getByUserId(Long userId) {
        List<Notification> list = notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
        list.forEach(n -> {
            if (n.getActivityId() != null) {
                activityRepository.findById(n.getActivityId()).ifPresent(a -> n.setActivityTitle(a.getTitle()));
            }
        });
        return Result.success(list);
    }

    public Result<Integer> getUnreadCount(Long userId) {
        int count = notificationRepository.countByUserIdAndIsRead(userId, 0);
        return Result.success(count);
    }

    public Result<?> markAsRead(Long notificationId) {
        return notificationRepository.findById(notificationId).map(n -> {
            n.setIsRead(1);
            notificationRepository.save(n);
            return Result.success("已读", null);
        }).orElse(Result.error("消息不存在"));
    }

    @Transactional
    public Result<?> markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
        return Result.success("全部已读", null);
    }
}