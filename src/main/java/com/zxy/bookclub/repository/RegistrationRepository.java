package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByActivityIdAndUserId(Long activityId, Long userId);
    List<Registration> findByActivityId(Long activityId);
    List<Registration> findByUserIdOrderByRegisterTimeDesc(Long userId);
    int countByActivityId(Long activityId);
    boolean existsByActivityIdAndUserId(Long activityId, Long userId);

    @Modifying
    @Query("DELETE FROM Registration r WHERE r.activityId = ?1")
    void deleteByActivityId(Long activityId);
}