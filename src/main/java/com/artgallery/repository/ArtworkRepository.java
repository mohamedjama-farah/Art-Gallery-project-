package com.artgallery.repository;

import java.util.List;
import java.util.Optional;

import com.artgallery.model.Artwork;

/**
 * Repository interface for Artwork persistence operations.
 */
public interface ArtworkRepository {
	/**
	 * Saves a new artwork.
	 * @param artwork the artwork to save (non-null)
	 */
	void save(Artwork artwork);

	/**
	 * Finds an artwork by ID.
	 * @param id the artwork ID (non-null)
	 * @return Optional containing the artwork if found
	 */
	Optional<Artwork> findById(String id);

	/**
	 * Finds all artworks.
	 * @return list of all artworks
	 */
	List<Artwork> findAll();

	/**
	 * Deletes an artwork by ID.
	 * @param id the artwork ID (non-null)
	 */
	void delete(String id);

	/**
	 * Updates an existing artwork.
	 * @param artwork the artwork to update (non-null)
	 */
	void update(Artwork artwork);
}
