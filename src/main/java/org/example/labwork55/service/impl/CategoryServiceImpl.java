package org.example.labwork55.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dao.CategoryDao;
import org.example.labwork55.dto.CategoryDto;
import org.example.labwork55.model.Category;
import org.example.labwork55.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryDao.getAllCategories().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(int id) {
        Category category = categoryDao.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return mapToDto(category);
    }

    @Override
    public int createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return categoryDao.createCategory(category);
    }

    @Override
    public void assignCategoryToQuiz(int quizId, int categoryId) {
        categoryDao.assignCategoryToQuiz(quizId, categoryId);
    }

    @Override
    public List<CategoryDto> getCategoriesByQuizId(int quizId) {
        return categoryDao.getCategoriesByQuizId(quizId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}