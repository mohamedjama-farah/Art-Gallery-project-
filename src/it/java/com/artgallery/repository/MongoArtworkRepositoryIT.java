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