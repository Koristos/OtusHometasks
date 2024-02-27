package ru.fefelov.summer.market.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fefelov.summer.market.repositories.CategoryRepository;
import ru.fefelov.summer.market.model.Category;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
}
