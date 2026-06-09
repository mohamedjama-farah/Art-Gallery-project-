package com.artgallery.controller;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import com.artgallery.model.Artwork;
import com.artgallery.repository.ArtworkRepository;
import com.artgallery.view.ArtGalleryView;
class ArtworkControllerTest {
    private ArtworkRepository mockRepository;
    private ArtGalleryView mockView;
    private ArtworkController controller;
    @BeforeEach void setUp() {
        mockRepository = mock(ArtworkRepository.class);
        mockView = mock(ArtGalleryView.class);
        controller = new ArtworkController(mockRepository, mockView);
    }
    @Test void testConstructorNullRepository() {
        assertThatThrownBy(() -> new ArtworkController(null, mockView))
            .isInstanceOf(NullPointerException.class).hasMessage("Repository cannot be null");
    }
    @Test void testConstructorNullView() {
        assertThatThrownBy(() -> new ArtworkController(mockRepository, null))
            .isInstanceOf(NullPointerException.class).hasMessage("View cannot be null");
    }
    @Test void testAllArtworksShowsAllArtworksInView() {
        List<Artwork> list = Arrays.asList(new Artwork("A","B",1.0), new Artwork("C","D",2.0));
        when(mockRepository.findAll()).thenReturn(list);
        controller.allArtworks();
        verify(mockView).showAllArtworks(list);
    }
    @Test void testAddArtworkWhenNotExistingSavesAndNotifiesView() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        when(mockRepository.findById(artwork.getId())).thenReturn(Optional.empty());
        controller.addArtwork(artwork);
        InOrder inOrder = inOrder(mockRepository, mockView);
        inOrder.verify(mockRepository).save(artwork);
        inOrder.verify(mockView).artworkAdded(artwork);
    }
    @Test void testAddArtworkWhenAlreadyExistingShowsError() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        when(mockRepository.findById(artwork.getId())).thenReturn(Optional.of(artwork));
        controller.addArtwork(artwork);
        verify(mockView).showError("Already existing artwork with id " + artwork.getId(), artwork);
        verifyNoMoreInteractions(mockView);
    }
    @Test void testAddNullArtworkThrowsException() {
        assertThatThrownBy(() -> controller.addArtwork(null))
            .isInstanceOf(NullPointerException.class).hasMessage("Artwork cannot be null");
    }
    @Test void testDeleteArtworkWhenExistingDeletesAndNotifiesView() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        artwork.setId("test-id-123");
        when(mockRepository.findById("test-id-123")).thenReturn(Optional.of(artwork));
        controller.deleteArtwork("test-id-123");
        InOrder inOrder = inOrder(mockRepository, mockView);
        inOrder.verify(mockRepository).delete("test-id-123");
        inOrder.verify(mockView).artworkRemoved(artwork);
    }