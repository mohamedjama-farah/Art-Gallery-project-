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