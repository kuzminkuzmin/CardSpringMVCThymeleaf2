package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.model.Card;
import com.dmitriikuzmin.model.Category;
import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.service.CardService;
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
public class CardController {
    private CardService cardService;
    private CategoryService categoryService;
    private UserService userService;

    @Autowired
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new/card/{categoryId}")
    public String newCard(@PathVariable long categoryId, Model model) {
        Category category = categoryService.get(categoryId);
        model.addAttribute("category", category);
        return "/operations/newCard";
    }

    @PostMapping("/save/card/{categoryId}")
    public String saveCard(@PathVariable long categoryId,
                           @RequestParam String question,
                           @RequestParam String answer,
                           Model model,
                           HttpSession session) {
        try {
            Category category = categoryService.get(categoryId);
            User user = (User) session.getAttribute("user");
            this.cardService.addCard(category.getId(), new Card(question, answer));
            user = this.userService.get(user.getId());
            model.addAttribute("user", user);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/operations/newCard";
        }
    }

    @GetMapping("/editCard/{id}")
    public String editCard(@PathVariable long id, Model model) {
        Card card = this.cardService.get(id);
        model.addAttribute("card", card);
        return "/operations/editCard";
    }

    @PostMapping("/updateCard")
    public String updateCard(@RequestParam long id,
                             @RequestParam String question,
                             @RequestParam String answer,
                             Model model,
                             HttpSession session) {
        Card card = this.cardService.get(id);
        card.setQuestion(question);
        card.setAnswer(answer);
        Category category = categoryService.get(card.getCategory().getId());
        User user = (User) session.getAttribute("user");
        try {
            this.cardService.update(card);
            user = this.userService.get(user.getId());
            model.addAttribute("user", user);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/operations/newCard";
        }
    }

    @GetMapping("/deleteCard/{id}")
    public String deleteCard(@PathVariable long id, Model model, HttpSession session) {
        Card card = this.cardService.get(id);
        User user = (User) session.getAttribute("user");
        try {
            this.cardService.delete(id);
            user = this.userService.get(user.getId());
            model.addAttribute("user", user);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }
}
