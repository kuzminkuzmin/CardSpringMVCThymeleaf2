package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.service.UserService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Mode model) {
        return "operations/login";
    }

    @PostMapping("/login/check")
    public String login(
            @RequestParam String login,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        try {
            User user = userService.get(login, password);
            model.addAttribute("user", user);
            session.setAttribute("user", user);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "operations/login";
        }
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        return "operations/registration";
    }

    @PostMapping("/registration/register")
    public String registration(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String name,
            Model model) {
        try {
            userService.addUser(login, password, name);
            return "operations/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "operations/registration";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
