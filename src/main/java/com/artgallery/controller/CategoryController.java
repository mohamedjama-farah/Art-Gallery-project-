package com.artgallery.controller;

import java.util.List;
import java.util.Objects;

import com.artgallery.model.Category;
import com.artgallery.repository.CategoryRepository;

public class CategoryController {
	private final CategoryRepository repository;

	public CategoryController(CategoryRepository repository) {
		this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
	}

	public void addCategory(Category category) {
		Objects.requireNonNull(category, "Category cannot be null");
		repository.save(category);
	}

	public Category getCategoryById(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
	}

	public List<Category> getAllCategories() {
		return repository.findAll();
	}

	public void deleteCategory(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		if (repository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Category not found: " + id);
		}
		repository.delete(id);
	}

	public void updateCategory(Category category) {
		Objects.requireNonNull(category, "Category cannot be null");
		if (repository.findById(category.getId()).isEmpty()) {
			throw new IllegalArgumentException("Category not found: " + category.getId());
		}
		repository.update(category);
	}
}
