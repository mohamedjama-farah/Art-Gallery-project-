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
	@Test
	@DisplayName("Should throw exception when name is blank")
	void testCategoryBlankName() {
		assertThatThrownBy(() -> new Category("   "))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Name cannot be blank");
	}
	@Test
	@DisplayName("Should set description")
	void testSetDescription() {
		Category category = new Category("Impressionism");
		category.setDescription("A 19th century art movement");
		assertThat(category.getDescription()).isEqualTo("A 19th century art movement");
	}
	@Test
	@DisplayName("Should generate and set ID")
	void testSetAndGetId() {
		Category category = new Category("Impressionism");
		String id = "507f1f77bcf86cd799439012";
		category.setId(id);
		assertThat(category.getId()).isEqualTo(id);
	}