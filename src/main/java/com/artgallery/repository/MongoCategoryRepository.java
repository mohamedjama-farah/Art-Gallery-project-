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

/**
 * MongoDB implementation of CategoryRepository.
 * Persists categories to a MongoDB database.
 */
public class MongoCategoryRepository implements CategoryRepository {
	private final MongoClient mongoClient;
	private final MongoDatabase database;
	private final MongoCollection<Document> collection;

	/**
	 * Creates a new MongoCategoryRepository with default database name.
	 * @param mongoClient the MongoDB client (non-null)
	 */
	public MongoCategoryRepository(MongoClient mongoClient) {
		this(mongoClient, "art-gallery");
	}

	/**
	 * Creates a new MongoCategoryRepository with specified database name.
	 * @param mongoClient the MongoDB client (non-null)
	 * @param databaseName the database name (non-null)
	 */
	public MongoCategoryRepository(MongoClient mongoClient, String databaseName) {
		this.mongoClient = Objects.requireNonNull(mongoClient, "MongoClient cannot be null");
		this.database = mongoClient.getDatabase(databaseName);
		this.collection = database.getCollection("categories");
	}

	/**
	 * Saves a new category to the database.
	 * @param category the category to save (non-null)
	 */
	@Override
	public void save(Category category) {
		Objects.requireNonNull(category, "Category cannot be null");
		Document doc = categoryToDocument(category);
		doc.put("_id", new ObjectId());
		collection.insertOne(doc);
		category.setId(doc.getObjectId("_id").toString());
	}

	/**
	 * Finds a category by ID.
	 * @param id the category ID (non-null)
	 * @return Optional containing the category if found
	 */
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

	/**
	 * Finds all categories in the database.
	 * @return list of all categories
	 */
	@Override
	public List<Category> findAll() {
		List<Category> categories = new ArrayList<>();
		collection.find().forEach(doc -> categories.add(documentToCategory(doc)));
		return categories;
	}

	/**
	 * Deletes a category by ID.
	 * @param id the category ID (non-null)
	 * @throws IllegalArgumentException if ID format is invalid
	 */
	@Override
	public void delete(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		try {
			collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid ID format: " + id);
		}
	}

	/**
	 * Updates an existing category.
	 * @param category the category to update (non-null)
	 * @throws IllegalArgumentException if category ID is invalid
	 */
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
