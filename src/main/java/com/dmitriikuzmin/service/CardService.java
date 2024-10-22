package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Card;

import java.util.List;

public interface CardService {
    void addCard(long categoryId, Card card);
    List<Card> getByUserId(long userId);
    List<Card> getByCategoryId(long categoryId);
    Card get(long id);
    Card update(Card card);
    Card delete(long id);
}
