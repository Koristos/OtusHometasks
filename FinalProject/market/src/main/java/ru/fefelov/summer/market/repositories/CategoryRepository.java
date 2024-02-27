package ru.fefelov.summer.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fefelov.summer.market.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
