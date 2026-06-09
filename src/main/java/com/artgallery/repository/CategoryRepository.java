package com.artgallery.repository;

import java.util.List;
import java.util.Optional;

import com.artgallery.model.Category;

public interface CategoryRepository {
	void save(Category category);

	Optional<Category> findById(String id);

	List<Category> findAll();

	void delete(String id);

	void update(Category category);
}
