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
    @Test void testDeleteArtworkWhenNotExistingShowsError() {
        when(mockRepository.findById("999")).thenReturn(Optional.empty());
        controller.deleteArtwork("999");
        verify(mockView).showError("No existing artwork with id 999", null);
        verifyNoMoreInteractions(mockView);
    }
    @Test void testDeleteNullIdThrowsException() {
        assertThatThrownBy(() -> controller.deleteArtwork(null))
            .isInstanceOf(NullPointerException.class).hasMessage("ID cannot be null");
    }
    @Test void testAllArtworksCallsViewWithCorrectList() {
        Artwork a1 = new Artwork("A1", "Artist1", 100.0);
        Artwork a2 = new Artwork("A2", "Artist2", 200.0);
        List<Artwork> list = Arrays.asList(a1, a2);
        when(mockRepository.findAll()).thenReturn(list);
        controller.allArtworks();
        verify(mockView).showAllArtworks(list);
    }
    @Test void testGetArtworkByIdReturnsCorrectArtwork() {
        Artwork artwork = new Artwork("Test", "Artist", 500.0);
        artwork.setId("test-123");
        when(mockRepository.findById("test-123")).thenReturn(Optional.of(artwork));
        Artwork result = controller.getArtworkById("test-123");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("test-123");
    }
    @Test void testGetArtworkByIdNotFoundThrowsException() {
        when(mockRepository.findById("nonexistent")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> controller.getArtworkById("nonexistent"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Artwork not found: nonexistent");
    }
    @Test void testGetAllArtworksReturnsRepositoryList() {
        Artwork a1 = new Artwork("A1", "Artist1", 100.0);
        Artwork a2 = new Artwork("A2", "Artist2", 200.0);
        List<Artwork> list = Arrays.asList(a1, a2);
        when(mockRepository.findAll()).thenReturn(list);
        List<Artwork> result = controller.getAllArtworks();
        assertThat(result).hasSize(2).containsExactly(a1, a2);
    }
    @Test void testUpdateArtworkWhenExistingUpdatesAndNotifiesView() {
        Artwork artwork = new Artwork("Updated", "Artist", 500.0);
        artwork.setId("123");
        when(mockRepository.findById("123")).thenReturn(Optional.of(artwork));
        controller.updateArtwork(artwork);
        verify(mockRepository).update(artwork);
    }
    @Test void testUpdateArtworkWhenNotExistingShowsError() {
        Artwork artwork = new Artwork("Test", "Artist", 100.0);
        artwork.setId("nonexistent");
        when(mockRepository.findById("nonexistent")).thenReturn(Optional.empty());
        controller.updateArtwork(artwork);
        verify(mockView).showError("No existing artwork with id nonexistent", artwork);
    }
    @Test void testUpdateNullArtworkThrowsException() {
        assertThatThrownBy(() -> controller.updateArtwork(null))
            .isInstanceOf(NullPointerException.class).hasMessage("Artwork cannot be null");
    }
    @Test void testGetNullIdThrowsException() {
        assertThatThrownBy(() -> controller.getArtworkById(null))
            .isInstanceOf(NullPointerException.class).hasMessage("ID cannot be null");
    }
    @Test void testAddArtworkWithIdNotExistingInRepository() {
        Artwork artwork = new Artwork("New", "Artist", 100.0);
        artwork.setId("new-id-123");
        when(mockRepository.findById("new-id-123")).thenReturn(Optional.empty());
        controller.addArtwork(artwork);
        verify(mockRepository).save(artwork);
        verify(mockView).artworkAdded(artwork);
        verify(mockView, never()).showError(anyString(), any());
    }
    @Test void testAddArtworkNeverShowsErrorWhenIdNotInRepository() {
        Artwork artwork = new Artwork("New", "Artist", 100.0);
        artwork.setId("test-id-not-in-repo");
        when(mockRepository.findById("test-id-not-in-repo")).thenReturn(Optional.empty());

        controller.addArtwork(artwork);

        InOrder order = inOrder(mockRepository, mockView);
        order.verify(mockRepository).findById("test-id-not-in-repo");
        order.verify(mockRepository).save(artwork);
        order.verify(mockView).artworkAdded(artwork);
        verify(mockView, never()).showError(anyString(), any());
    }
    @Test void testAddArtworkWithNullIdSkipsFindById() {
        Artwork artwork = new Artwork("Test", "Artist", 100.0);
        artwork.setId(null);  // Explicitly set to null to test the getId() != null branch

        controller.addArtwork(artwork);

        // When ID is null, condition fails at first part (short-circuit), so we just save
        verify(mockRepository, never()).findById(null);
        verify(mockRepository).save(artwork);
        verify(mockView).artworkAdded(artwork);
    }
}
