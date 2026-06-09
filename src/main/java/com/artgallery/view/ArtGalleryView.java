package com.artgallery.view;
import java.util.List;
import com.artgallery.model.Artwork;
public interface ArtGalleryView {
    void showAllArtworks(List<Artwork> artworks);
    void artworkAdded(Artwork artwork);
    void artworkRemoved(Artwork artwork);
    void showError(String message, Artwork artwork);
}
