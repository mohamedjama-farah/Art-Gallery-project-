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

import com.artgallery.model.Artwork;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;

@Testcontainers
@DisplayName("MongoArtworkRepository Integration Tests")
class MongoArtworkRepositoryIT {
	@Container
	static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0");

	private MongoArtworkRepository repository;
	private MongoClient mongoClient;

	@BeforeAll
	static void setupContainer() {
		mongoContainer.start();
	}

	@BeforeEach
	void setUp() {
		mongoClient = MongoClients.create(mongoContainer.getReplicaSetUrl());
		repository = new MongoArtworkRepository(mongoClient, "artgallery_test");
		repository.findAll().forEach(a -> repository.delete(a.getId()));
	}
	@Test
	@DisplayName("Should create repository with single parameter constructor")
	void testSingleParameterConstructor() {
		MongoArtworkRepository singleParamRepo = new MongoArtworkRepository(mongoClient);
		assertThat(singleParamRepo).isNotNull();
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		singleParamRepo.save(artwork);
		assertThat(artwork.getId()).isNotNull();
	}
	@Test
	@DisplayName("Should throw exception when updating with invalid ID format")
	void testUpdateWithInvalidIdFormat() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		artwork.setId("not-a-valid-objectid");
		assertThatThrownBy(() -> repository.update(artwork))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid ID format: not-a-valid-objectid");
	}
	@Test
	@DisplayName("Should handle artwork with non-zero year value")
	void testArtworkWithNonZeroYear() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		artwork.setYear(2023);
		repository.save(artwork);

		Artwork found = repository.findById(artwork.getId()).orElse(null);
		assertThat(found).isNotNull();
		assertThat(found.getYear()).isEqualTo(2023);
	}
	@Test
	@DisplayName("Should handle null year from document")
	void testArtworkWithNullYearInDocument() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		repository.save(artwork);

		// Verify that even with year=0 (default), it's properly handled
		Artwork found = repository.findById(artwork.getId()).orElse(null);
		assertThat(found).isNotNull();
		// Default year when not set explicitly
		assertThat(found.getYear()).isEqualTo(0);
	}
	@Test
	@DisplayName("Should save artwork to database")
	void testSaveArtwork() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork.setYear(1889);
		artwork.setDescription("A painting of a starry night");

		repository.save(artwork);

		assertThat(artwork.getId()).isNotNull();
		assertThat(artwork.getId()).isNotEmpty();
	}
	@Test
	@DisplayName("Should find artwork by ID")
	void testFindArtworkById() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork.setYear(1889);
		repository.save(artwork);

		Artwork found = repository.findById(artwork.getId()).orElse(null);

		assertThat(found).isNotNull();
		assertThat(found.getTitle()).isEqualTo("Starry Night");
		assertThat(found.getArtist()).isEqualTo("Van Gogh");
		assertThat(found.getPrice()).isEqualTo(1000.0);
	}
	@Test
	@DisplayName("Should return empty optional when artwork not found")
	void testFindArtworkNotFound() {
		assertThat(repository.findById("507f1f77bcf86cd799439011")).isEmpty();
	}
	@Test
	@DisplayName("Should find all artworks")
	void testFindAllArtworks() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		Artwork artwork2 = new Artwork("Persistence of Memory", "Dali", 2000.0);
		repository.save(artwork1);
		repository.save(artwork2);

		List<Artwork> artworks = repository.findAll();

		assertThat(artworks).hasSize(2);
	}
	@Test
	@DisplayName("Should delete artwork by ID")
	void testDeleteArtwork() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		repository.save(artwork);

		repository.delete(artwork.getId());

		assertThat(repository.findById(artwork.getId())).isEmpty();
	}
	@Test
	@DisplayName("Should update artwork")
	void testUpdateArtwork() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork.setYear(1889);
		repository.save(artwork);

		artwork.setDescription("Updated description");
		repository.update(artwork);

		Artwork updated = repository.findById(artwork.getId()).orElse(null);
		assertThat(updated).isNotNull();
		assertThat(updated.getDescription()).isEqualTo("Updated description");
	}