package com.dmitriikuzmin.repository;

import com.dmitriikuzmin.model.Category;
import com.dmitriikuzmin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}
