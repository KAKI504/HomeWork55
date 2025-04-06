package org.example.labwork55.service;

import org.example.labwork55.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(int id);
    int createCategory(CategoryDto categoryDto);
    void assignCategoryToQuiz(int quizId, int categoryId);
    List<CategoryDto> getCategoriesByQuizId(int quizId);
}