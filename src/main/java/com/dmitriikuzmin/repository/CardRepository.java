package com.dmitriikuzmin.repository;

import com.dmitriikuzmin.model.Card;
import com.dmitriikuzmin.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByCategory(Category category);

    List<Card> findByCategoryIn(List<Category> categories);
}
