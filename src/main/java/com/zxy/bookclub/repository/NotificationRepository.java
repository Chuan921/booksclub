package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreateTimeDesc(Long userId);
    int countByUserIdAndIsRead(Long userId, Integer isRead);
    boolean existsByUserIdAndActivityIdAndType(Long userId, Long activityId, String type);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = 1 WHERE n.userId = ?1")
    void markAllAsRead(Long userId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.activityId = ?1")
    void deleteByActivityId(Long activityId);
}