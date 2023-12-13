package com.ra.taskmanagement.service;

import com.ra.taskmanagement.entity.Category;
import com.ra.taskmanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    @Override
    public Category createCategory(Category category) {
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category updatedCategory) {
        Optional<Category> categoryToUpdate = categoryRepository.findById(id);

        if (categoryToUpdate.isPresent()) {
            Category existingCategory = categoryToUpdate.get();
            existingCategory.setName(updatedCategory.getName());
            existingCategory.setTasks(updatedCategory.getTasks());
            existingCategory.setUpdatedAt(LocalDateTime.now());

            return categoryRepository.save(existingCategory);
        }

        return null;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
