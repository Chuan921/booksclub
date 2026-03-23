package com.zxy.bookclub.service;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Activity;
import com.zxy.bookclub.entity.CheckIn;
import com.zxy.bookclub.repository.ActivityRepository;
import com.zxy.bookclub.repository.CheckInRepository;
import com.zxy.bookclub.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CheckInService {
    @Autowired
    private CheckInRepository checkInRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    public Result<?> checkIn(Long activityId, Long userId) {
        // 1. 验证活动是否存在
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) return Result.error("活动不存在");

        // 2. 验证用户是否已报名
        if (!registrationRepository.existsByActivityIdAndUserId(activityId, userId)) {
            return Result.error("您未报名此活动");
        }

        // 3. 验证签到时间窗口
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = activity.getStartTime();
        LocalDateTime endTime = activity.getEndTime();
        LocalDateTime checkInStart = startTime.minusMinutes(10);

        if (now.isBefore(checkInStart)) {
            return Result.error("签到尚未开始，请在活动开始前10分钟签到");
        }
        if (now.isAfter(endTime)) {
            return Result.error("活动已结束，无法签到");
        }

        // 4. 检查是否已签到
        if (checkInRepository.existsByActivityIdAndUserId(activityId, userId)) {
            return Result.error("您已签到，请勿重复操作");
        }

        // 5. 创建签到记录
        CheckIn checkIn = new CheckIn();
        checkIn.setActivityId(activityId);
        checkIn.setUserId(userId);
        checkIn.setCheckInTime(now);
        checkInRepository.save(checkIn);

        return Result.success("签到成功", checkIn);
    }

    public Result<Boolean> getCheckInStatus(Long activityId, Long userId) {
        boolean checked = checkInRepository.existsByActivityIdAndUserId(activityId, userId);
        return Result.success(checked);
    }
}