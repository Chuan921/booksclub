package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByActivityIdAndUserId(Long activityId, Long userId);
    boolean existsByActivityIdAndUserId(Long activityId, Long userId);
    int countByActivityId(Long activityId);

    @Modifying
    @Query("DELETE FROM CheckIn c WHERE c.activityId = ?1")
    void deleteByActivityId(Long activityId);
}