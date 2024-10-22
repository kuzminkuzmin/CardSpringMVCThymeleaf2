package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Category;

import java.util.List;

public interface CategoryService {
    void addCategory(long userId, Category category);
    List<Category> getByUserId(long userId);
    Category get(long id);
    Category update(Category category);
    Category delete(long id);
}
