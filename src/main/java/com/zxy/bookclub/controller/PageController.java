package com.zxy.bookclub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register() { return "register"; }

    @GetMapping("/activities")
    public String activities() { return "activities"; }

    @GetMapping("/activity/{id}")
    public String activityDetail(@PathVariable Long id) { return "activity-detail"; }

    @GetMapping("/create-activity")
    public String createActivity() { return "create-activity"; }

    @GetMapping("/edit-activity/{id}")
    public String editActivity(@PathVariable Long id) { return "create-activity"; }

    @GetMapping("/my-activities")
    public String myActivities() { return "my-activities"; }

    @GetMapping("/my-registrations")
    public String myRegistrations() { return "my-registrations"; }

    @GetMapping("/profile")
    public String profile() { return "profile"; }

    @GetMapping("/my-favorites")
    public String myFavorites() { return "my-favorites"; }

    @GetMapping("/notifications")
    public String notifications() { return "notifications"; }
}