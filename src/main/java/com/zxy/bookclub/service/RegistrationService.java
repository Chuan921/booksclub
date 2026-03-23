package com.zxy.bookclub.service;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Activity;
import com.zxy.bookclub.entity.Registration;
import com.zxy.bookclub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private CheckInRepository checkInRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityLikeRepository activityLikeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    public Result<?> register(Long activityId, Long userId) {
        if (registrationRepository.existsByActivityIdAndUserId(activityId, userId)) {
            return Result.error("您已报名此活动");
        }
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) return Result.error("活动不存在");

        // 检查活动是否已结束（时间已过或状态不是ACTIVE）
        if (activity.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.error("活动已结束，无法报名");
        }
        if (!"ACTIVE".equals(activity.getStatus())) {
            return Result.error("活动已结束，无法报名");
        }

        int current = registrationRepository.countByActivityId(activityId);
        if (activity.getMaxParticipants() > 0 && current >= activity.getMaxParticipants()) {
            return Result.error("报名人数已满");
        }

        Registration reg = new Registration();
        reg.setActivityId(activityId);
        reg.setUserId(userId);
        registrationRepository.save(reg);
        return Result.success("报名成功", reg);
    }

    public Result<?> cancel(Long activityId, Long userId) {
        return registrationRepository.findByActivityIdAndUserId(activityId, userId)
                .map(reg -> {
                    registrationRepository.delete(reg);
                    return Result.success("取消报名成功", null);
                }).orElse(Result.error("未找到报名记录"));
    }

    public Result<List<Registration>> getMyRegistrations(Long userId) {
        List<Registration> list = registrationRepository.findByUserIdOrderByRegisterTimeDesc(userId);
        list.forEach(reg -> {
            activityRepository.findById(reg.getActivityId()).ifPresent(a -> {
                userRepository.findById(a.getCreatorId()).ifPresent(u ->
                        a.setCreatorName(u.getNickname() != null ? u.getNickname() : u.getUsername()));
                a.setCurrentParticipants(registrationRepository.countByActivityId(a.getId()));
                a.setLikeCount(activityLikeRepository.countByActivityId(a.getId()));
                a.setLiked(activityLikeRepository.existsByActivityIdAndUserId(a.getId(), userId));
                a.setFavorited(favoriteRepository.existsByActivityIdAndUserId(a.getId(), userId));
                reg.setActivity(a);
            });
            reg.setCheckedIn(checkInRepository.existsByActivityIdAndUserId(reg.getActivityId(), userId));
        });
        return Result.success(list);
    }

    public Result<?> getActivityRegistrations(Long activityId, Long userId) {
        // 验证用户是否是活动发起人
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) return Result.error("活动不存在");
        if (!activity.getCreatorId().equals(userId)) return Result.error("无权限查看");

        // 获取报名列表并填充用户信息
        List<Registration> list = registrationRepository.findByActivityId(activityId);
        list.forEach(reg -> {
            userRepository.findById(reg.getUserId()).ifPresent(u ->
                    reg.setUserName(u.getNickname() != null ? u.getNickname() : u.getUsername()));
            reg.setCheckedIn(checkInRepository.existsByActivityIdAndUserId(activityId, reg.getUserId()));
        });
        return Result.success(list);
    }
}