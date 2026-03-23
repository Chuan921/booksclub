package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByOrderByCreateTimeDesc();

    List<Activity> findByCreatorIdOrderByCreateTimeDesc(Long creatorId);

    // 定时任务需要：查询特定时间范围内的活动
    List<Activity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // 搜索功能：支持关键字和类型搜索
    @Query("SELECT a FROM Activity a WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "a.title LIKE %:keyword% OR a.description LIKE %:keyword% OR a.location LIKE %:keyword%) " +
            "AND (:type IS NULL OR :type = '' OR a.type = :type) " +
            "ORDER BY a.createTime DESC")
    List<Activity> searchActivities(@Param("keyword") String keyword, @Param("type") String type);
}