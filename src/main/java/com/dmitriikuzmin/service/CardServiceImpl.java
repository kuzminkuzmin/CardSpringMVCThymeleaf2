package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Card;
import com.dmitriikuzmin.model.Category;
import com.dmitriikuzmin.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private CategoryService categoryService;

    @Autowired
    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public void addCard(long categoryId, Card card) {
        Category category = this.categoryService.get(categoryId);
        card.setCategory(category);
        try {
            this.cardRepository.save(card);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Card already exists");
        }
    }

    @Override
    public List<Card> getByUserId(long userId) {
        List<Category> categories = this.categoryService.getByUserId(userId);
        return this.cardRepository.findByCategoryIn(categories);
    }

    @Override
    public List<Card> getByCategoryId(long categoryId) {
        Category category = this.categoryService.get(categoryId);
        return this.cardRepository.findByCategory(category);
    }

    @Override
    public Card get(long id) {
        return this.cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }

    @Override
    public Card update(Card card) {
        Card base = this.get(card.getId());
        base.setQuestion(card.getQuestion());
        base.setAnswer(card.getAnswer());
        try {
            this.cardRepository.save(base);
            return base;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Card already exists");
        }
    }

    @Override
    public Card delete(long id) {
        Card card = this.get(id);
        this.cardRepository.deleteById(id);
        return card;
    }
}
