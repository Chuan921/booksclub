package com.zxy.bookclub.controller;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.Comment;
import com.zxy.bookclub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result<Comment> add(@RequestBody Map<String, Object> body) {
        Long activityId = Long.valueOf(body.get("activityId").toString());
        Long userId = Long.valueOf(body.get("userId").toString());
        String content = body.get("content").toString();
        return commentService.add(activityId, userId, content);
    }

    @GetMapping("/activity/{activityId}")
    public Result<List<Comment>> getByActivityId(@PathVariable Long activityId,
                                                 @RequestParam(required = false) Long userId) {
        return commentService.getByActivityId(activityId, userId);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, @RequestParam Long userId) {
        return commentService.delete(id, userId);
    }
}