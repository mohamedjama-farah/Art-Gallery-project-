package com.artgallery.view;
import java.util.List;
import com.artgallery.model.Artwork;

/**
 * Interface for the Art Gallery user interface.
 * Provides methods for displaying artworks and error messages.
 */
public interface ArtGalleryView {
    /**
     * Displays all artworks.
     * @param artworks list of artworks to display
     */
    void showAllArtworks(List<Artwork> artworks);

    /**
     * Displays a notification that an artwork was added.
     * @param artwork the artwork that was added
     */
    void artworkAdded(Artwork artwork);

    /**
     * Displays a notification that an artwork was removed.
     * @param artwork the artwork that was removed
     */
    void artworkRemoved(Artwork artwork);

    /**
     * Displays an error message.
     * @param message the error message
     * @param artwork optional artwork related to the error
     */
    void showError(String message, Artwork artwork);
}
