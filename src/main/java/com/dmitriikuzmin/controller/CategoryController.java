package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.model.Category;
import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.service.CategoryService;
import com.dmitriikuzmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class CategoryController {
    private CategoryService categoryService;
    private UserService userService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new/category")
    public String newCategory(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return "operations/newCategory";
    }

    @PostMapping("/save/category")
    public String saveCategory(@RequestParam String name, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        User user = (User) session.getAttribute("user");
        try {
            this.categoryService.addCategory(user.getId(), new Category(name));
            user = this.userService.get(user.getId());
            model.addAttribute("user", user);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorCategory", e.getMessage());
            return "operations/newCategory";
        }
    }

    @GetMapping("/editCategory/{id}")
    public String editCategory(@PathVariable long id, Model model) {
        Category category = this.categoryService.get(id);
        model.addAttribute("category", category);
        return "operations/editCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@RequestParam long id, @RequestParam String name, Model model) {
        Category category = this.categoryService.get(id);
        category.setName(name);
        try {
            this.categoryService.update(category);
            User user = this.userService.get(category.getUser().getId());
            model.addAttribute("user", user);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorCategory", e.getMessage());
            return "operations/editCategory";
        }
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable long id, Model model) {
        Category category = this.categoryService.get(id);
        try {
            this.categoryService.delete(id);
            User user = this.userService.get(category.getUser().getId());
            model.addAttribute("user", user);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorCategory", e.getMessage());
            return "index";
        }
    }
}