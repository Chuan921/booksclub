package com.zxy.bookclub.controller;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {
    @Autowired
    private CheckInService checkInService;

    @PostMapping
    public Result<?> checkIn(@RequestParam Long activityId, @RequestParam Long userId) {
        return checkInService.checkIn(activityId, userId);
    }

    @GetMapping("/status")
    public Result<Boolean> getStatus(@RequestParam Long activityId, @RequestParam Long userId) {
        return checkInService.getCheckInStatus(activityId, userId);
    }
}