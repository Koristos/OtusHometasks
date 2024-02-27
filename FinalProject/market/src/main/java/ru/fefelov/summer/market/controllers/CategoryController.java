package ru.fefelov.summer.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.fefelov.summer.market.exceptions.ResourceNotFoundException;
import ru.fefelov.summer.market.model.Category;
import ru.fefelov.summer.market.services.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public Category findById(@PathVariable Long id) {
        Category c = categoryService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found, id: " + id));
        return c;
    }
}
