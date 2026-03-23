package com.zxy.bookclub.controller;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Registration;
import com.zxy.bookclub.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public Result<?> register(@RequestParam Long activityId, @RequestParam Long userId) {
        return registrationService.register(activityId, userId);
    }

    @DeleteMapping
    public Result<?> cancel(@RequestParam Long activityId, @RequestParam Long userId) {
        return registrationService.cancel(activityId, userId);
    }

    @GetMapping("/my")
    public Result<List<Registration>> getMyRegistrations(@RequestParam Long userId) {
        return registrationService.getMyRegistrations(userId);
    }

    @GetMapping("/activity/{activityId}")
    public Result<?> getActivityRegistrations(@PathVariable Long activityId, @RequestParam Long userId) {
        return registrationService.getActivityRegistrations(activityId, userId);
    }
}