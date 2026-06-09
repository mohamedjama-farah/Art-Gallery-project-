package com.artgallery.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.artgallery.model.Category;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Testcontainers
@DisplayName("MongoCategoryRepository Integration Tests")
class MongoCategoryRepositoryIT {
	@Container
	static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0");

	private MongoCategoryRepository repository;
	private MongoClient mongoClient;

	@BeforeAll
	static void setupContainer() {
		mongoContainer.start();
	}

	@BeforeEach
	void setUp() {
		mongoClient = MongoClients.create(mongoContainer.getReplicaSetUrl());
		repository = new MongoCategoryRepository(mongoClient, "artgallery_test");
		repository.findAll().forEach(c -> repository.delete(c.getId()));
	}
	@Test
	@DisplayName("Should create repository with single parameter constructor")
	void testSingleParameterConstructor() {
		MongoCategoryRepository singleParamRepo = new MongoCategoryRepository(mongoClient);
		assertThat(singleParamRepo).isNotNull();
		Category category = new Category("Test Category");
		singleParamRepo.save(category);
		assertThat(category.getId()).isNotNull();
	}
	@Test
	@DisplayName("Should throw exception when updating with invalid ID format")
	void testUpdateWithInvalidIdFormat() {
		Category category = new Category("Test");
		category.setId("not-a-valid-objectid");
		assertThatThrownBy(() -> repository.update(category))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid ID format: not-a-valid-objectid");
	}
	@Test
	@DisplayName("Should save category to database")
	void testSaveCategory() {
		Category category = new Category("Painting");
		category.setDescription("Oil and watercolor paintings");

		repository.save(category);

		assertThat(category.getId()).isNotNull();
		assertThat(category.getId()).isNotEmpty();
	}
	@Test
	@DisplayName("Should find category by ID")
	void testFindCategoryById() {
		Category category = new Category("Sculpture");
		category.setDescription("Stone and bronze sculptures");
		repository.save(category);

		Category found = repository.findById(category.getId()).orElse(null);

		assertThat(found).isNotNull();
		assertThat(found.getName()).isEqualTo("Sculpture");
		assertThat(found.getDescription()).isEqualTo("Stone and bronze sculptures");
	}
	@Test
	@DisplayName("Should return empty optional when category not found")
	void testFindCategoryNotFound() {
		assertThat(repository.findById("507f1f77bcf86cd799439011")).isEmpty();
	}
	@Test
	@DisplayName("Should find all categories")
	void testFindAllCategories() {
		Category category1 = new Category("Modern Art");
		Category category2 = new Category("Classical Art");
		repository.save(category1);
		repository.save(category2);

		List<Category> categories = repository.findAll();

		assertThat(categories).hasSize(2);
	}
	@Test
	@DisplayName("Should delete category by ID")
	void testDeleteCategory() {
		Category category = new Category("Contemporary");
		repository.save(category);

		repository.delete(category.getId());

		assertThat(repository.findById(category.getId())).isEmpty();
	}
	@Test
	@DisplayName("Should update category")
	void testUpdateCategory() {
		Category category = new Category("Abstract");
		category.setDescription("Original description");
		repository.save(category);

		category.setDescription("Updated description");
		repository.update(category);

		Category updated = repository.findById(category.getId()).orElse(null);
		assertThat(updated).isNotNull();
		assertThat(updated.getDescription()).isEqualTo("Updated description");
	}
	@Test
	@DisplayName("Should handle empty database")
	void testFindAllEmptyDatabase() {
		List<Category> categories = repository.findAll();
		assertThat(categories).isEmpty();
	}
	@Test
	@DisplayName("Should save and retrieve category with null description")
	void testCategoryWithNullDescription() {
		Category category = new Category("Impressionism");
		repository.save(category);

		Category found = repository.findById(category.getId()).orElse(null);
		assertThat(found).isNotNull();
		assertThat(found.getName()).isEqualTo("Impressionism");
		assertThat(found.getDescription()).isNull();
	}
	@Test
	@DisplayName("Should throw exception when saving null category")
	void testSaveNullCategory() {
		assertThatThrownBy(() -> repository.save(null))
			.isInstanceOf(NullPointerException.class);
	}
	@Test
	@DisplayName("Should throw exception when finding null ID")
	void testFindNullId() {
		assertThatThrownBy(() -> repository.findById(null))
			.isInstanceOf(NullPointerException.class);
	}
	@Test
	@DisplayName("Should throw exception when deleting null ID")
	void testDeleteNullId() {
		assertThatThrownBy(() -> repository.delete(null))
			.isInstanceOf(NullPointerException.class);
	}
	@Test
	@DisplayName("Should throw exception when updating null category")
	void testUpdateNullCategory() {
		assertThatThrownBy(() -> repository.update(null))
			.isInstanceOf(NullPointerException.class);
	}