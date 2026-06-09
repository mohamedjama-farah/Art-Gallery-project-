package com.artgallery.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Category Model Tests")
class CategoryTest {
	@Test
	@DisplayName("Should create category with valid name")
	void testCategoryCreation() {
		Category category = new Category("Impressionism");
		assertThat(category.getName()).isEqualTo("Impressionism");
	}
	@Test
	@DisplayName("Should throw exception when name is null")
	void testCategoryNullName() {
		assertThatThrownBy(() -> new Category(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Name cannot be null");
	}