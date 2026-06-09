package com.artgallery.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.artgallery.model.Artwork;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoArtworkRepository implements ArtworkRepository {
	private final MongoClient mongoClient;
	private final MongoDatabase database;
	private final MongoCollection<Document> collection;

	public MongoArtworkRepository(MongoClient mongoClient) {
		this(mongoClient, "art-gallery");
	}

	public MongoArtworkRepository(MongoClient mongoClient, String databaseName) {
		this.mongoClient = Objects.requireNonNull(mongoClient, "MongoClient cannot be null");
		this.database = mongoClient.getDatabase(databaseName);
		this.collection = database.getCollection("artworks");
	}

	@Override
	public void save(Artwork artwork) {
		Objects.requireNonNull(artwork, "Artwork cannot be null");
		Document doc = artworkToDocument(artwork);
		doc.put("_id", new ObjectId());
		collection.insertOne(doc);
		artwork.setId(doc.getObjectId("_id").toString());
	}

	@Override
	public Optional<Artwork> findById(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		try {
			Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
			if (doc != null) {
				return Optional.of(documentToArtwork(doc));
			}
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public List<Artwork> findAll() {
		List<Artwork> artworks = new ArrayList<>();
		collection.find().forEach(doc -> artworks.add(documentToArtwork(doc)));
		return artworks;
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
	public void update(Artwork artwork) {
		Objects.requireNonNull(artwork, "Artwork cannot be null");
		Objects.requireNonNull(artwork.getId(), "Artwork ID cannot be null");
		Document doc = artworkToDocument(artwork);
		try {
			collection.replaceOne(Filters.eq("_id", new ObjectId(artwork.getId())), doc);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid ID format: " + artwork.getId());
		}
	}

	private Document artworkToDocument(Artwork artwork) {
		Document doc = new Document();
		doc.put("title", artwork.getTitle());
		doc.put("artist", artwork.getArtist());
		doc.put("price", artwork.getPrice());
		doc.put("year", artwork.getYear());
		doc.put("description", artwork.getDescription());
		return doc;
	}

	private Artwork documentToArtwork(Document doc) {
		Artwork artwork = new Artwork(doc.getString("title"), doc.getString("artist"), doc.getDouble("price"));
		artwork.setId(doc.getObjectId("_id").toString());
		Integer year = doc.getInteger("year");
		if (year != null && year != 0) {
			artwork.setYear(year);
		}
		artwork.setDescription(doc.getString("description"));
		return artwork;
	}
}
