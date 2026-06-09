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
		assertThat(found.getYear()).isZero();
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
	@Test
	@DisplayName("Should save null artwork throws exception")
	void testSaveNullArtwork() {
		assertThatThrownBy(() -> repository.save(null))
			.isInstanceOf(NullPointerException.class);
	}
	@Test
	@DisplayName("Should find null ID throws exception")
	void testFindNullId() {
		assertThatThrownBy(() -> repository.findById(null))
			.isInstanceOf(NullPointerException.class);
	}
	@Test
	@DisplayName("Should delete null ID throws exception")
	void testDeleteNullId() {
		assertThatThrownBy(() -> repository.delete(null))
			.isInstanceOf(NullPointerException.class);
	}
	@Test
	@DisplayName("Should update null artwork throws exception")
	void testUpdateNullArtwork() {
		assertThatThrownBy(() -> repository.update(null))
			.isInstanceOf(NullPointerException.class);
	}
	@Test
	@DisplayName("Should handle invalid ID format in findById")
	void testFindByIdInvalidFormat() {
		assertThat(repository.findById("not-a-valid-id")).isEmpty();
	}
	@Test
	@DisplayName("Should handle invalid ID format in delete")
	void testDeleteInvalidIdFormat() {
		assertThatThrownBy(() -> repository.delete("not-a-valid-id"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid ID format: not-a-valid-id");
	}
	@Test
	@DisplayName("Should handle empty list when no artworks exist")
	void testFindAllWhenEmpty() {
		List<Artwork> artworks = repository.findAll();
		assertThat(artworks).isEmpty();
	}
	@Test
	@DisplayName("Should preserve all artwork fields when saving")
	void testSavePreservesAllFields() {
		Artwork artwork = new Artwork("Test Title", "Test Artist", 500.0);
		artwork.setYear(2000);
		artwork.setDescription("Test Description");

		repository.save(artwork);
		Artwork found = repository.findById(artwork.getId()).orElse(null);

		assertThat(found).isNotNull();
		assertThat(found.getTitle()).isEqualTo("Test Title");
		assertThat(found.getArtist()).isEqualTo("Test Artist");
		assertThat(found.getPrice()).isEqualTo(500.0);
		assertThat(found.getYear()).isEqualTo(2000);
		assertThat(found.getDescription()).isEqualTo("Test Description");
	}
	@Test
	@DisplayName("Should update only description field")
	void testUpdateOnlyDescription() {
		Artwork artwork = new Artwork("Original", "Artist", 100.0);
		artwork.setYear(2000);
		artwork.setDescription("Original Description");
		repository.save(artwork);

		artwork.setDescription("Updated Description");
		repository.update(artwork);

		Artwork found = repository.findById(artwork.getId()).orElse(null);
		assertThat(found).isNotNull();
		assertThat(found.getDescription()).isEqualTo("Updated Description");
		assertThat(found.getTitle()).isEqualTo("Original");
		assertThat(found.getYear()).isEqualTo(2000);
	}
	@Test
	@DisplayName("Should handle artwork with zero year and verify title preserved")
	void testArtworkWithZeroYear() {
		Artwork artwork = new Artwork("ZeroYearArtwork", "Artist", 100.0);
		// Year defaults to 0, don't set it
		repository.save(artwork);

		Artwork found = repository.findById(artwork.getId()).orElse(null);
		assertThat(found).isNotNull();
		assertThat(found.getYear()).isZero();
		assertThat(found.getTitle()).isEqualTo("ZeroYearArtwork");
	}
	@Test
	@DisplayName("Should handle artwork with null description")
	void testArtworkWithNullDescription() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		artwork.setDescription(null);
		repository.save(artwork);

		Artwork found = repository.findById(artwork.getId()).orElse(null);
		assertThat(found).isNotNull();
		assertThat(found.getDescription()).isNull();
	}
	@Test
	@DisplayName("Should update artwork with null description")
	void testUpdateArtworkWithNullDescription() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		artwork.setDescription("Original");
		repository.save(artwork);

		artwork.setDescription(null);
		repository.update(artwork);

		Artwork found = repository.findById(artwork.getId()).orElse(null);
		assertThat(found).isNotNull();
		assertThat(found.getDescription()).isNull();
	}
	@Test
	@DisplayName("Should find all returns same objects after save")
	void testFindAllReturnsSavedObjects() {
		Artwork a1 = new Artwork("A1", "Artist1", 100.0);
		Artwork a2 = new Artwork("A2", "Artist2", 200.0);
		repository.save(a1);
		repository.save(a2);

		List<Artwork> all = repository.findAll();
		assertThat(all).hasSize(2);
		assertThat(all).extracting(Artwork::getTitle).contains("A1", "A2");
	}
	@Test
	@DisplayName("Should handle specific year value in documentToArtwork")
	void testArtworkWithSpecificYear() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		artwork.setYear(1999);  // Set to specific year to ensure setYear is called
		repository.save(artwork);

		Artwork found = repository.findById(artwork.getId()).orElse(null);
		assertThat(found).isNotNull();
		// Verify year was set and retrieved correctly
		assertThat(found.getYear()).isEqualTo(1999);
	}
	@Test
	@DisplayName("Should handle null year field in document")
	void testDocumentWithNullYearField() {
		// Manually create and insert a document with null year
		Document doc = new Document()
			.append("title", "Null Year")
			.append("artist", "Artist")
			.append("price", 100.0)
			.append("year", null)
			.append("description", "Test");

		com.mongodb.client.MongoCollection<Document> collection =
			mongoClient.getDatabase("artgallery_test").getCollection("artworks");
		collection.insertOne(doc);

		String id = doc.getObjectId("_id").toString();
		Artwork found = repository.findById(id).orElse(null);

		assertThat(found).isNotNull();
		// When year is null, setYear should not be called, year defaults to 0
		assertThat(found.getYear()).isZero();
	}
}
