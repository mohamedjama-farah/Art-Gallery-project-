package com.artgallery.view.swing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import com.artgallery.controller.ArtworkController;
import com.artgallery.model.Artwork;
public class ArtGalleryFrameTest {
    private ArtGalleryFrame frame;
    private ArtworkController mockController;
    @BeforeEach public void setUp() {
        mockController = mock(ArtworkController.class);
        doNothing().when(mockController).addArtwork(new Artwork("Test", "Test", 0.0));
        frame = new ArtGalleryFrame();
        frame.setController(mockController);
    }
    @Test public void testAddButtonIsDisabledWhenFieldsAreEmpty() {
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test public void testAddButtonIsEnabledWhenAllFieldsFilled() {
        frame.titleTextField.setText("Starry Night");
        frame.artistTextField.setText("Van Gogh");
        frame.priceTextField.setText("1000.0");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test public void testAddButtonDisabledWhenTitleEmpty() {
        frame.artistTextField.setText("Van Gogh");
        frame.priceTextField.setText("1000.0");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test public void testAddButtonDisabledWhenArtistEmpty() {
        frame.titleTextField.setText("Starry Night");
        frame.priceTextField.setText("1000.0");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test public void testAddButtonDisabledWhenPriceEmpty() {
        frame.titleTextField.setText("Starry Night");
        frame.artistTextField.setText("Van Gogh");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }