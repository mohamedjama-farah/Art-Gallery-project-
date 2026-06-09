package com.artgallery.repository;

import java.util.List;
import java.util.Optional;

import com.artgallery.model.Artwork;

public interface ArtworkRepository {
	void save(Artwork artwork);

	Optional<Artwork> findById(String id);

	List<Artwork> findAll();

	void delete(String id);

	void update(Artwork artwork);
}
