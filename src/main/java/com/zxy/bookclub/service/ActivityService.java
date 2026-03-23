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
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private ActivityLikeRepository activityLikeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CheckInRepository checkInRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public Result<Activity> create(Activity activity) {
        activityRepository.save(activity);
        return Result.success("创建成功", activity);
    }

    public Result<List<Activity>> getAll(Long currentUserId) {
        List<Activity> list = activityRepository.findAllByOrderByCreateTimeDesc();
        list.forEach(a -> enrichActivity(a, currentUserId));
        return Result.success(list);
    }

    // 新增：搜索活动
    public Result<List<Activity>> searchActivities(String keyword, String type, Long currentUserId) {
        List<Activity> list = activityRepository.searchActivities(keyword, type);
        list.forEach(a -> enrichActivity(a, currentUserId));
        return Result.success(list);
    }

    public Result<List<Activity>> getByCreator(Long creatorId) {
        List<Activity> list = activityRepository.findByCreatorIdOrderByCreateTimeDesc(creatorId);
        list.forEach(a -> enrichActivity(a, creatorId));
        return Result.success(list);
    }

    // 管理员获取所有活动
    public Result<List<Activity>> getAllForAdmin(Long adminId) {
        List<Activity> list = activityRepository.findAllByOrderByCreateTimeDesc();
        list.forEach(a -> enrichActivity(a, adminId));
        return Result.success(list);
    }

    public Result<Activity> getById(Long id, Long currentUserId) {
        return activityRepository.findById(id).map(a -> {
            enrichActivity(a, currentUserId);
            return Result.success(a);
        }).orElse(Result.error("活动不存在"));
    }

    public Result<Activity> update(Long id, Activity updated, Long userId) {
        return activityRepository.findById(id).map(activity -> {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) return Result.<Activity>error("用户不存在");
            // 权限校验
            if (!"ADMIN".equals(user.getRole()) && !activity.getCreatorId().equals(userId)) {
                return Result.<Activity>error("无权限修改此活动");
            }
            activity.setTitle(updated.getTitle());
            activity.setDescription(updated.getDescription());
            activity.setType(updated.getType());
            activity.setLocation(updated.getLocation());
            activity.setStartTime(updated.getStartTime());
            activity.setEndTime(updated.getEndTime());
            activity.setMaxParticipants(updated.getMaxParticipants());
            activityRepository.save(activity);
            return Result.success("更新成功", activity);
        }).orElse(Result.error("活动不存在"));
    }

    @Transactional
    public Result<String> delete(Long id, Long userId) {
        Activity activity = activityRepository.findById(id).orElse(null);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!"ADMIN".equals(user.getRole()) && !activity.getCreatorId().equals(userId)) {
            return Result.error("无权限删除此活动");
        }

        // 级联删除关联数据
        // 1. 删除评论的点赞（需要先获取评论ID）
        List<Comment> comments = commentRepository.findByActivityIdOrderByCreateTimeDesc(id);
        for (Comment comment : comments) {
            commentLikeRepository.deleteByCommentId(comment.getId());
        }
        // 2. 删除评论
        commentRepository.deleteByActivityId(id);
        // 3. 删除活动点赞
        activityLikeRepository.deleteByActivityId(id);
        // 4. 删除收藏
        favoriteRepository.deleteByActivityId(id);
        // 5. 删除签到记录
        checkInRepository.deleteByActivityId(id);
        // 6. 删除报名记录
        registrationRepository.deleteByActivityId(id);
        // 7. 删除相关通知
        notificationRepository.deleteByActivityId(id);
        // 8. 最后删除活动
        activityRepository.deleteById(id);

        return Result.success("删除成功", "deleted");
    }

    private void enrichActivity(Activity a, Long userId) {
        userRepository.findById(a.getCreatorId()).ifPresent(u -> a.setCreatorName(u.getNickname() != null ? u.getNickname() : u.getUsername()));
        a.setCurrentParticipants(registrationRepository.countByActivityId(a.getId()));
        a.setLikeCount(activityLikeRepository.countByActivityId(a.getId()));
        if (userId != null) {
            a.setLiked(activityLikeRepository.existsByActivityIdAndUserId(a.getId(), userId));
            a.setFavorited(favoriteRepository.existsByActivityIdAndUserId(a.getId(), userId));
            a.setRegistered(registrationRepository.existsByActivityIdAndUserId(a.getId(), userId));
        }
    }
}