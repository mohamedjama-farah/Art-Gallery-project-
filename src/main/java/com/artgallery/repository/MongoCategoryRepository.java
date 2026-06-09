package com.artgallery.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.artgallery.model.Category;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoCategoryRepository implements CategoryRepository {
	private final MongoClient mongoClient;
	private final MongoDatabase database;
	private final MongoCollection<Document> collection;

	public MongoCategoryRepository(MongoClient mongoClient) {
		this(mongoClient, "art-gallery");
	}

	public MongoCategoryRepository(MongoClient mongoClient, String databaseName) {
		this.mongoClient = Objects.requireNonNull(mongoClient, "MongoClient cannot be null");
		this.database = mongoClient.getDatabase(databaseName);
		this.collection = database.getCollection("categories");
	}

	@Override
	public void save(Category category) {
		Objects.requireNonNull(category, "Category cannot be null");
		Document doc = categoryToDocument(category);
		doc.put("_id", new ObjectId());
		collection.insertOne(doc);
		category.setId(doc.getObjectId("_id").toString());
	}

	@Override
	public Optional<Category> findById(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		try {
			Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
			if (doc != null) {
				return Optional.of(documentToCategory(doc));
			}
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public List<Category> findAll() {
		List<Category> categories = new ArrayList<>();
		collection.find().forEach(doc -> categories.add(documentToCategory(doc)));
		return categories;
	}

	@Override
	public void delete(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		try {
			collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid ID format: " + id);
		}
	}

	@Override
	public void update(Category category) {
		Objects.requireNonNull(category, "Category cannot be null");
		Objects.requireNonNull(category.getId(), "Category ID cannot be null");
		Document doc = categoryToDocument(category);
		try {
			collection.replaceOne(Filters.eq("_id", new ObjectId(category.getId())), doc);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid ID format: " + category.getId());
		}
	}

	private Document categoryToDocument(Category category) {
		Document doc = new Document();
		doc.put("name", category.getName());
		doc.put("description", category.getDescription());
		return doc;
	}

	private Category documentToCategory(Document doc) {
		Category category = new Category(doc.getString("name"));
		category.setId(doc.getObjectId("_id").toString());
		category.setDescription(doc.getString("description"));
		return category;
	}
}
