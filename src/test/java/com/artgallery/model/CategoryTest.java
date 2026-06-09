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
	@Test
	@DisplayName("Should be equal when all fields match")
	void testEquality() {
		Category category1 = new Category("Impressionism");
		category1.setDescription("A 19th century art movement");
		Category category2 = new Category("Impressionism");
		category2.setDescription("A 19th century art movement");
		assertThat(category1).isEqualTo(category2);
	}
	@Test
	@DisplayName("Should not be equal when names differ")
	void testInequality() {
		Category category1 = new Category("Impressionism");
		Category category2 = new Category("Modern Art");
		assertThat(category1).isNotEqualTo(category2);
	}
	@Test
	@DisplayName("Should handle hashCode correctly")
	void testHashCode() {
		Category category1 = new Category("Impressionism");
		category1.setDescription("Desc1");
		Category category2 = new Category("Impressionism");
		category2.setDescription("Desc1");
		assertThat(category1.hashCode()).isEqualTo(category2.hashCode());
	}
	@Test
	@DisplayName("Should not be equal to null")
	void testEqualityWithNull() {
		Category category = new Category("Impressionism");
		assertThat(category).isNotEqualTo(null);
	}
	@Test
	@DisplayName("Should not be equal to different type")
	void testEqualityWithDifferentType() {
		Category category = new Category("Impressionism");
		assertThat(category).isNotEqualTo("Not a category");
	}
	@Test
	@DisplayName("Should not be equal when description differs")
	void testInequalityWithDifferentDescription() {
		Category category1 = new Category("Impressionism");
		category1.setDescription("Desc1");
		Category category2 = new Category("Impressionism");
		category2.setDescription("Desc2");
		assertThat(category1).isNotEqualTo(category2);
	}