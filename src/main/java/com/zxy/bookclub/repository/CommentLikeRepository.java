package com.zxy.bookclub.repository;

import com.zxy.bookclub.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    int countByCommentId(Long commentId);

    @Modifying
    @Query("DELETE FROM CommentLike cl WHERE cl.commentId = ?1")
    void deleteByCommentId(Long commentId);
}