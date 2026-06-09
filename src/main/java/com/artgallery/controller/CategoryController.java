package com.artgallery.controller;

import java.util.List;
import java.util.Objects;

import com.artgallery.model.Category;
import com.artgallery.repository.CategoryRepository;

/**
 * Controller for managing category operations.
 * Coordinates between the repository and business logic layers.
 */
public class CategoryController {
	private final CategoryRepository repository;

	/**
	 * Creates a new CategoryController.
	 * @param repository the category repository (non-null)
	 */
	public CategoryController(CategoryRepository repository) {
		this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
	}

	/**
	 * Adds a new category.
	 * @param category the category to add (non-null)
	 */
	public void addCategory(Category category) {
		Objects.requireNonNull(category, "Category cannot be null");
		repository.save(category);
	}

	/**
	 * Gets a category by ID.
	 * @param id the category ID (non-null)
	 * @return the category
	 * @throws IllegalArgumentException if category not found
	 */
	public Category getCategoryById(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
	}

	/**
	 * Gets all categories.
	 * @return list of all categories
	 */
	public List<Category> getAllCategories() {
		return repository.findAll();
	}

	/**
	 * Deletes a category by ID.
	 * @param id the category ID (non-null)
	 * @throws IllegalArgumentException if category not found
	 */
	public void deleteCategory(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		if (repository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Category not found: " + id);
		}
		repository.delete(id);
	}

	/**
	 * Updates an existing category.
	 * @param category the category to update (non-null)
	 * @throws IllegalArgumentException if category not found
	 */
	public void updateCategory(Category category) {
		Objects.requireNonNull(category, "Category cannot be null");
		if (repository.findById(category.getId()).isEmpty()) {
			throw new IllegalArgumentException("Category not found: " + category.getId());
		}
		repository.update(category);
	}
}
