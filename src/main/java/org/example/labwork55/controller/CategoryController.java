package org.example.labwork55.controller;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dto.CategoryDto;
import org.example.labwork55.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable int categoryId) {
        try {
            return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto, Principal principal) {
        try {
            int categoryId = categoryService.createCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", categoryId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getCategoriesByQuizId(@PathVariable int quizId) {
        try {
            return ResponseEntity.ok(categoryService.getCategoriesByQuizId(quizId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}