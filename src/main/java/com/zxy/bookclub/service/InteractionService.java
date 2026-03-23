package com.zxy.bookclub.service;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.*;
import com.zxy.bookclub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InteractionService {
    @Autowired
    private ActivityLikeRepository activityLikeRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;

    // 活动点赞/取消点赞
    public Result<?> toggleActivityLike(Long activityId, Long userId) {
        return activityLikeRepository.findByActivityIdAndUserId(activityId, userId)
                .map(like -> {
                    activityLikeRepository.delete(like);
                    return Result.success("取消点赞", false);
                }).orElseGet(() -> {
                    ActivityLike like = new ActivityLike();
                    like.setActivityId(activityId);
                    like.setUserId(userId);
                    activityLikeRepository.save(like);
                    // 通知活动创建者
                    activityRepository.findById(activityId).ifPresent(activity -> {
                        if (!activity.getCreatorId().equals(userId)) {
                            userRepository.findById(userId).ifPresent(liker -> {
                                String name = liker.getNickname() != null ? liker.getNickname() : liker.getUsername();
                                notificationService.create(activity.getCreatorId(), activityId, "LIKE_ACTIVITY",
                                        name + " 赞了您的活动「" + activity.getTitle() + "」");
                            });
                        }
                    });
                    return Result.success("点赞成功", true);
                });
    }

    // 评论点赞/取消点赞
    public Result<?> toggleCommentLike(Long commentId, Long userId) {
        return commentLikeRepository.findByCommentIdAndUserId(commentId, userId)
                .map(like -> {
                    commentLikeRepository.delete(like);
                    return Result.success("取消点赞", false);
                }).orElseGet(() -> {
                    CommentLike like = new CommentLike();
                    like.setCommentId(commentId);
                    like.setUserId(userId);
                    commentLikeRepository.save(like);
                    // 通知评论作者
                    commentRepository.findById(commentId).ifPresent(comment -> {
                        if (!comment.getUserId().equals(userId)) {
                            userRepository.findById(userId).ifPresent(liker -> {
                                String name = liker.getNickname() != null ? liker.getNickname() : liker.getUsername();
                                notificationService.create(comment.getUserId(), comment.getActivityId(), "LIKE_COMMENT",
                                        name + " 赞了您的评论");
                            });
                        }
                    });
                    return Result.success("点赞成功", true);
                });
    }

    // 收藏/取消收藏
    public Result<?> toggleFavorite(Long activityId, Long userId) {
        return favoriteRepository.findByActivityIdAndUserId(activityId, userId)
                .map(fav -> {
                    favoriteRepository.delete(fav);
                    return Result.success("取消收藏", false);
                }).orElseGet(() -> {
                    Favorite fav = new Favorite();
                    fav.setActivityId(activityId);
                    fav.setUserId(userId);
                    favoriteRepository.save(fav);
                    return Result.success("收藏成功", true);
                });
    }

    // 获取我的收藏
    public Result<List<Favorite>> getMyFavorites(Long userId) {
        List<Favorite> list = favoriteRepository.findByUserIdOrderByCreateTimeDesc(userId);
        list.forEach(fav -> activityRepository.findById(fav.getActivityId()).ifPresent(a -> {
            userRepository.findById(a.getCreatorId()).ifPresent(u ->
                    a.setCreatorName(u.getNickname() != null ? u.getNickname() : u.getUsername()));
            fav.setActivity(a);
        }));
        return Result.success(list);
    }
}