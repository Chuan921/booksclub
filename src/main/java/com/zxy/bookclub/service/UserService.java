package com.zxy.bookclub.service;

import com.zxy.bookclub.dto.Result;
import com.zxy.bookclub.entity.User;
import com.zxy.bookclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Result<User> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return Result.error("用户名已存在");
        }
        user.setRole("USER");
        userRepository.save(user);
        return Result.success("注册成功", user);
    }

    public Result<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .map(u -> Result.success("登录成功", u))
                .orElse(Result.error("用户名或密码错误"));
    }

    public Result<User> getById(Long id) {
        return userRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("用户不存在"));
    }

    public Result<User> updateProfile(Long id, User updated) {
        return userRepository.findById(id).map(user -> {
            if (updated.getNickname() != null) user.setNickname(updated.getNickname());
            if (updated.getRealName() != null) user.setRealName(updated.getRealName());
            if (updated.getPhone() != null) user.setPhone(updated.getPhone());
            userRepository.save(user);
            return Result.success("更新成功", user);
        }).orElse(Result.error("用户不存在"));
    }
}