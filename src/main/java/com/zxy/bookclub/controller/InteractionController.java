package com.zxy.bookclub.controller;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Favorite;
import com.zxy.bookclub.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interaction")
public class InteractionController {
    @Autowired
    private InteractionService interactionService;

    @PostMapping("/like/activity")
    public Result<?> toggleActivityLike(@RequestParam Long activityId, @RequestParam Long userId) {
        return interactionService.toggleActivityLike(activityId, userId);
    }

    @PostMapping("/like/comment")
    public Result<?> toggleCommentLike(@RequestParam Long commentId, @RequestParam Long userId) {
        return interactionService.toggleCommentLike(commentId, userId);
    }

    @PostMapping("/favorite")
    public Result<?> toggleFavorite(@RequestParam Long activityId, @RequestParam Long userId) {
        return interactionService.toggleFavorite(activityId, userId);
    }

    @GetMapping("/favorites")
    public Result<List<Favorite>> getMyFavorites(@RequestParam Long userId) {
        return interactionService.getMyFavorites(userId);
    }
}