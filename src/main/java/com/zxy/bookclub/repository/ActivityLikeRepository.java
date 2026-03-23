package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.ActivityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ActivityLikeRepository extends JpaRepository<ActivityLike, Long> {
    Optional<ActivityLike> findByActivityIdAndUserId(Long activityId, Long userId);
    boolean existsByActivityIdAndUserId(Long activityId, Long userId);
    int countByActivityId(Long activityId);

    @Modifying
    @Query("DELETE FROM ActivityLike al WHERE al.activityId = ?1")
    void deleteByActivityId(Long activityId);
}