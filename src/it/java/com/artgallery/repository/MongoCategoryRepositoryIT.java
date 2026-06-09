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