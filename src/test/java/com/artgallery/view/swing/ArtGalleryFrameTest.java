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
    @Test public void testShowAllArtworksPopulatesList() {
        Artwork a1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
        Artwork a2 = new Artwork("The Kiss", "Klimt", 2000.0);
        frame.showAllArtworks(Arrays.asList(a1, a2));
        assertThat(frame.listModel.size()).isEqualTo(2);
        assertThat(frame.listModel.get(0)).contains("Starry Night");
        assertThat(frame.listModel.get(1)).contains("The Kiss");
    }
    @Test public void testArtworkAddedAppendsToList() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        assertThat(frame.listModel.size()).isEqualTo(1);
        assertThat(frame.listModel.get(0)).contains("Starry Night");
    }
    @Test public void testArtworkRemovedRemovesFromList() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        frame.artworkRemoved(artwork);
        assertThat(frame.listModel.size()).isEqualTo(0);
    }
    @Test public void testShowErrorDisplaysMessage() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.showError("Already existing", artwork);
        assertThat(frame.errorLabel.getText()).contains("Already existing");
    }
    @Test public void testShowErrorWithoutArtwork() {
        frame.showError("Error message", null);
        assertThat(frame.errorLabel.getText()).isEqualTo("Error message");
    }
    @Test public void testClearFieldsAfterArtworkAdded() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.titleTextField.setText("Starry Night");
        frame.artistTextField.setText("Van Gogh");
        frame.priceTextField.setText("1000.0");
        frame.artworkAdded(artwork);
        assertThat(frame.titleTextField.getText()).isEmpty();
        assertThat(frame.artistTextField.getText()).isEmpty();
        assertThat(frame.priceTextField.getText()).isEmpty();
    }
    @Test public void testShowAllArtworksEmptyList() {
        frame.showAllArtworks(Arrays.asList());
        assertThat(frame.listModel.size()).isEqualTo(0);
    }
    @Test public void testMultipleArtworksAdded() {
        Artwork a1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
        Artwork a2 = new Artwork("The Kiss", "Klimt", 2000.0);
        frame.artworkAdded(a1);
        frame.artworkAdded(a2);
        assertThat(frame.listModel.size()).isEqualTo(2);
    }
    @Test public void testErrorLabelClearedAfterArtworkAdded() {
        frame.errorLabel.setText("Some error");
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        assertThat(frame.errorLabel.getText()).isEqualTo(" ");
    }
    @Test public void testErrorLabelClearedAfterArtworkRemoved() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        frame.errorLabel.setText("Some error");
        frame.artworkRemoved(artwork);
        assertThat(frame.errorLabel.getText()).isEqualTo(" ");
    }
    @Test public void testButtonEnableStateWithWhitespaceOnly() {
        frame.titleTextField.setText("   ");
        frame.artistTextField.setText("   ");
        frame.priceTextField.setText("   ");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }