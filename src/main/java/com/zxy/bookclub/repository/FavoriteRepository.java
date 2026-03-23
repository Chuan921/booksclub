package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByActivityIdAndUserId(Long activityId, Long userId);
    boolean existsByActivityIdAndUserId(Long activityId, Long userId);
    List<Favorite> findByUserIdOrderByCreateTimeDesc(Long userId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.activityId = ?1")
    void deleteByActivityId(Long activityId);
}