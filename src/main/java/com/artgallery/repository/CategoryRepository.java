package com.artgallery.repository;

import java.util.List;
import java.util.Optional;

import com.artgallery.model.Category;

/**
 * Repository interface for Category persistence operations.
 */
public interface CategoryRepository {
	/**
	 * Saves a new category.
	 * @param category the category to save (non-null)
	 */
	void save(Category category);

	/**
	 * Finds a category by ID.
	 * @param id the category ID (non-null)
	 * @return Optional containing the category if found
	 */
	Optional<Category> findById(String id);

	/**
	 * Finds all categories.
	 * @return list of all categories
	 */
	List<Category> findAll();

	/**
	 * Deletes a category by ID.
	 * @param id the category ID (non-null)
	 */
	void delete(String id);

	/**
	 * Updates an existing category.
	 * @param category the category to update (non-null)
	 */
	void update(Category category);
}
