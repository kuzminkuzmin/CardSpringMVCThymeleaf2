package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Category;
import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private UserService userService;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addCategory(long userId, Category category) {
        User user = this.userService.get(userId);
        category.setUser(user);
        try {
            this.categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Category already exists");
        }
    }

    @Override
    public List<Category> getByUserId(long userId) {
        User user = this.userService.get(userId);
        return this.categoryRepository.findByUser(user);
    }

    @Override
    public Category get(long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Override
    public Category update(Category category) {
        Category base = this.get(category.getId());
        base.setName(category.getName());
        try {
            categoryRepository.save(base);
            return base;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Category already exists");
        }
    }

    @Override
    public Category delete(long id) {
        Category category = this.get(id);
        this.categoryRepository.deleteById(id);
        return category;
    }
}
