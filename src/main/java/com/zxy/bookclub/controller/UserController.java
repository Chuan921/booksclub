package com.zxy.bookclub.controller;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.User;
import com.zxy.bookclub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public Result<User> updateProfile(@PathVariable Long id, @RequestBody User user) {
        return userService.updateProfile(id, user);
    }
}