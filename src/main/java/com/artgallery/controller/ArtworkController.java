package com.artgallery.controller;
import java.util.List;
import java.util.Objects;
import com.artgallery.model.Artwork;
import com.artgallery.repository.ArtworkRepository;
import com.artgallery.view.ArtGalleryView;
public class ArtworkController {
    private final ArtworkRepository repository;
    private final ArtGalleryView view;
    public ArtworkController(ArtworkRepository repository, ArtGalleryView view) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }
    public void allArtworks() {
        view.showAllArtworks(repository.findAll());
    }
    public void addArtwork(Artwork artwork) {
        Objects.requireNonNull(artwork, "Artwork cannot be null");
        if (artwork.getId() != null && repository.findById(artwork.getId()).isPresent()) {
            view.showError("Already existing artwork with id " + artwork.getId(), artwork);
            return;
        }
        repository.save(artwork);
        view.artworkAdded(artwork);
    }
    public void deleteArtwork(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        if (repository.findById(id).isEmpty()) {
            view.showError("No existing artwork with id " + id, null);
            return;
        }
        Artwork artwork = repository.findById(id).get();
        repository.delete(id);
        view.artworkRemoved(artwork);
    }
    public List<Artwork> getAllArtworks() {
        return repository.findAll();
    }
    public Artwork getArtworkById(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Artwork not found: " + id));
    }
    public void updateArtwork(Artwork artwork) {
        Objects.requireNonNull(artwork, "Artwork cannot be null");
        if (repository.findById(artwork.getId()).isEmpty()) {
            view.showError("No existing artwork with id " + artwork.getId(), artwork);
            return;
        }
        repository.update(artwork);
    }
}
