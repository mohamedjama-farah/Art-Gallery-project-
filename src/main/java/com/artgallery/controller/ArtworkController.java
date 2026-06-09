package com.artgallery.controller;
import java.util.List;
import java.util.Objects;
import com.artgallery.model.Artwork;
import com.artgallery.repository.ArtworkRepository;
import com.artgallery.view.ArtGalleryView;

/**
 * Controller for managing artwork operations.
 * Coordinates between the view and repository layers.
 */
public class ArtworkController {
    private final ArtworkRepository repository;
    private final ArtGalleryView view;

    /**
     * Creates a new ArtworkController.
     * @param repository the artwork repository (non-null)
     * @param view the gallery view (non-null)
     */
    public ArtworkController(ArtworkRepository repository, ArtGalleryView view) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    /**
     * Displays all artworks in the view.
     */
    public void allArtworks() {
        view.showAllArtworks(repository.findAll());
    }

    /**
     * Adds a new artwork.
     * @param artwork the artwork to add (non-null)
     */
    public void addArtwork(Artwork artwork) {
        Objects.requireNonNull(artwork, "Artwork cannot be null");
        if (artwork.getId() != null && repository.findById(artwork.getId()).isPresent()) {
            view.showError("Already existing artwork with id " + artwork.getId(), artwork);
            return;
        }
        repository.save(artwork);
        view.artworkAdded(artwork);
    }

    /**
     * Deletes an artwork by ID.
     * @param id the artwork ID (non-null)
     */
    public void deleteArtwork(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        java.util.Optional<Artwork> found = repository.findById(id);
        if (!found.isPresent()) {
            view.showError("No existing artwork with id " + id, null);
            return;
        }
        repository.delete(id);
        view.artworkRemoved(found.get());
    }

    /**
     * Gets all artworks.
     * @return list of all artworks
     */
    public List<Artwork> getAllArtworks() {
        return repository.findAll();
    }

    /**
     * Gets an artwork by ID.
     * @param id the artwork ID (non-null)
     * @return the artwork
     * @throws IllegalArgumentException if artwork not found
     */
    public Artwork getArtworkById(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Artwork not found: " + id));
    }

    /**
     * Updates an existing artwork.
     * @param artwork the artwork to update (non-null)
     */
    public void updateArtwork(Artwork artwork) {
        Objects.requireNonNull(artwork, "Artwork cannot be null");
        if (repository.findById(artwork.getId()).isEmpty()) {
            view.showError("No existing artwork with id " + artwork.getId(), artwork);
            return;
        }
        repository.update(artwork);
    }
}
