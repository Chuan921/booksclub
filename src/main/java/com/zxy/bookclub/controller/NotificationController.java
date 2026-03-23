package com.zxy.bookclub.controller;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Notification;
import com.zxy.bookclub.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public Result<List<Notification>> getList(@RequestParam Long userId) {
        return notificationService.getByUserId(userId);
    }

    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount(@RequestParam Long userId) {
        return notificationService.getUnreadCount(userId);
    }

    @PutMapping("/{id}/read")
    public Result<?> markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    @PutMapping("/read-all")
    public Result<?> markAllAsRead(@RequestParam Long userId) {
        return notificationService.markAllAsRead(userId);
    }
}