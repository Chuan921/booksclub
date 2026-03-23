package com.zxy.bookclub.controller;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Activity;
import com.zxy.bookclub.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @PostMapping
    public Result<Activity> create(@RequestBody Activity activity, @RequestParam Long userId) {
        activity.setCreatorId(userId);
        return activityService.create(activity);
    }

    @GetMapping("/list")
    public Result<List<Activity>> getAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {
        // 如果有搜索条件，使用搜索方法；否则使用原来的方法
        if ((keyword != null && !keyword.trim().isEmpty()) || (type != null && !type.trim().isEmpty())) {
            return activityService.searchActivities(keyword, type, userId);
        }
        return activityService.getAll(userId);
    }

    @GetMapping("/my")
    public Result<List<Activity>> getMyActivities(@RequestParam Long userId) {
        return activityService.getByCreator(userId);
    }

    @GetMapping("/admin/all")
    public Result<List<Activity>> getAllForAdmin(@RequestParam Long userId) {
        return activityService.getAllForAdmin(userId);
    }

    @GetMapping("/{id}")
    public Result<Activity> getById(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        return activityService.getById(id, userId);
    }

    @PutMapping("/{id}")
    public Result<Activity> update(@PathVariable Long id, @RequestBody Activity activity, @RequestParam Long userId) {
        return activityService.update(id, activity, userId);
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id, @RequestParam Long userId) {
        return activityService.delete(id, userId);
    }
}