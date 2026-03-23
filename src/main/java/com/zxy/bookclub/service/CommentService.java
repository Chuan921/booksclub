package com.zxy.bookclub.service;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Activity;
import com.zxy.bookclub.entity.Comment;
import com.zxy.bookclub.entity.User;
import com.zxy.bookclub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private NotificationService notificationService;

    public Result<Comment> add(Long activityId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setActivityId(activityId);
        comment.setUserId(userId);
        comment.setContent(content);
        commentRepository.save(comment);

        // 通知活动创建者
        activityRepository.findById(activityId).ifPresent(activity -> {
            if (!activity.getCreatorId().equals(userId)) {
                userRepository.findById(userId).ifPresent(commenter -> {
                    String name = commenter.getNickname() != null ? commenter.getNickname() : commenter.getUsername();
                    notificationService.create(activity.getCreatorId(), activityId, "COMMENT",
                            name + " 评论了您的活动「" + activity.getTitle() + "」");
                });
            }
        });

        return Result.success("评论成功", comment);
    }

    public Result<List<Comment>> getByActivityId(Long activityId, Long currentUserId) {
        List<Comment> list = commentRepository.findByActivityIdOrderByCreateTimeDesc(activityId);
        list.forEach(c -> {
            userRepository.findById(c.getUserId()).ifPresent(u ->
                    c.setUserName(u.getNickname() != null ? u.getNickname() : u.getUsername()));
            c.setLikeCount(commentLikeRepository.countByCommentId(c.getId()));
            c.setLiked(currentUserId != null && commentLikeRepository.existsByCommentIdAndUserId(c.getId(), currentUserId));
        });
        return Result.success(list);
    }

    @Transactional
    public Result<?> delete(Long commentId, Long userId) {
        return commentRepository.findById(commentId).map(comment -> {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) return Result.error("用户不存在");
            if (!"ADMIN".equals(user.getRole()) && !comment.getUserId().equals(userId)) {
                return Result.error("无权限删除此评论");
            }

            // 先删除评论的所有点赞记录（关键修复）
            commentLikeRepository.deleteByCommentId(commentId);

            // 再删除评论本身
            commentRepository.delete(comment);

            return Result.success("删除成功", null);
        }).orElse(Result.error("评论不存在"));
    }
}