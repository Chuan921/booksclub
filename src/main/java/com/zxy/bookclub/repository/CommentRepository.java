package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByActivityIdOrderByCreateTimeDesc(Long activityId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.activityId = ?1")
    void deleteByActivityId(Long activityId);
}