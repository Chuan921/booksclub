package com.zxy.bookclub.service;

import com.zxy.bookclub.entity.Activity;
import com.zxy.bookclub.entity.Registration;
import com.zxy.bookclub.repository.ActivityRepository;
import com.zxy.bookclub.repository.NotificationRepository;
import com.zxy.bookclub.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    // 每分钟执行一次
    @Scheduled(cron = "0 * * * * ?")
    public void sendActivityReminders() {
        LocalDateTime now = LocalDateTime.now();

        // 1. 提前1天提醒
        LocalDateTime target1Day = now.plusHours(24);
        List<Activity> activities1Day = activityRepository.findByStartTimeBetween(target1Day, target1Day.plusMinutes(1));
        for (Activity activity : activities1Day) {
            sendReminder(activity, "REMINDER_1DAY", "您报名的活动「" + activity.getTitle() + "」将于明天开始，请做好准备！");
        }

        // 2. 提前10分钟提醒（签到提醒）
        LocalDateTime target10Min = now.plusMinutes(10);
        List<Activity> activities10Min = activityRepository.findByStartTimeBetween(target10Min, target10Min.plusMinutes(1));
        for (Activity activity : activities10Min) {
            sendReminder(activity, "REMINDER_10MIN", "活动「" + activity.getTitle() + "」即将开始，签到通道已开启，请前往签到！");
        }
    }

    private void sendReminder(Activity activity, String type, String content) {
        List<Registration> registrations = registrationRepository.findByActivityId(activity.getId());
        for (Registration reg : registrations) {
            boolean exists = notificationRepository.existsByUserIdAndActivityIdAndType(reg.getUserId(), activity.getId(), type);
            if (!exists) {
                notificationService.create(reg.getUserId(), activity.getId(), type, content);
            }
        }
    }
}