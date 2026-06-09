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
import com.artgallery.controller.ArtworkController;
import com.artgallery.model.Artwork;
public class ArtGalleryFrameTest {
    private ArtGalleryFrame frame;
    private ArtworkController mockController;
    @BeforeEach void setUp() {
        mockController = mock(ArtworkController.class);
        doNothing().when(mockController).addArtwork(new Artwork("Test", "Test", 0.0));
        frame = new ArtGalleryFrame();
        frame.setController(mockController);
    }
    @Test void testAddButtonIsDisabledWhenFieldsAreEmpty() {
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testAddButtonIsEnabledWhenAllFieldsFilled() {
        frame.titleTextField.setText("Starry Night");
        frame.artistTextField.setText("Van Gogh");
        frame.priceTextField.setText("1000.0");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testAddButtonDisabledWhenTitleEmpty() {
        frame.artistTextField.setText("Van Gogh");
        frame.priceTextField.setText("1000.0");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testAddButtonDisabledWhenArtistEmpty() {
        frame.titleTextField.setText("Starry Night");
        frame.priceTextField.setText("1000.0");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testAddButtonDisabledWhenPriceEmpty() {
        frame.titleTextField.setText("Starry Night");
        frame.artistTextField.setText("Van Gogh");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testShowAllArtworksPopulatesList() {
        Artwork a1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
        Artwork a2 = new Artwork("The Kiss", "Klimt", 2000.0);
        frame.showAllArtworks(Arrays.asList(a1, a2));
        assertThat(frame.listModel.size()).isEqualTo(2);
        assertThat(frame.listModel.get(0)).contains("Starry Night");
        assertThat(frame.listModel.get(1)).contains("The Kiss");
    }
    @Test void testArtworkAddedAppendsToList() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        assertThat(frame.listModel.size()).isEqualTo(1);
        assertThat(frame.listModel.get(0)).contains("Starry Night");
    }
    @Test void testArtworkRemovedRemovesFromList() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        frame.artworkRemoved(artwork);
        assertThat(frame.listModel.size()).isZero();
    }
    @Test void testShowErrorDisplaysMessage() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.showError("Already existing", artwork);
        assertThat(frame.errorLabel.getText()).contains("Already existing");
    }
    @Test void testShowErrorWithoutArtwork() {
        frame.showError("Error message", null);
        assertThat(frame.errorLabel.getText()).isEqualTo("Error message");
    }
    @Test void testClearFieldsAfterArtworkAdded() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.titleTextField.setText("Starry Night");
        frame.artistTextField.setText("Van Gogh");
        frame.priceTextField.setText("1000.0");
        frame.artworkAdded(artwork);
        assertThat(frame.titleTextField.getText()).isEmpty();
        assertThat(frame.artistTextField.getText()).isEmpty();
        assertThat(frame.priceTextField.getText()).isEmpty();
    }
    @Test void testShowAllArtworksEmptyList() {
        frame.showAllArtworks(Arrays.asList());
        assertThat(frame.listModel.size()).isZero();
    }
    @Test void testMultipleArtworksAdded() {
        Artwork a1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
        Artwork a2 = new Artwork("The Kiss", "Klimt", 2000.0);
        frame.artworkAdded(a1);
        frame.artworkAdded(a2);
        assertThat(frame.listModel.size()).isEqualTo(2);
    }
    @Test void testErrorLabelClearedAfterArtworkAdded() {
        frame.errorLabel.setText("Some error");
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        assertThat(frame.errorLabel.getText()).isEqualTo(" ");
    }
    @Test void testErrorLabelClearedAfterArtworkRemoved() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        frame.artworkAdded(artwork);
        frame.errorLabel.setText("Some error");
        frame.artworkRemoved(artwork);
        assertThat(frame.errorLabel.getText()).isEqualTo(" ");
    }
    @Test void testButtonEnableStateWithWhitespaceOnly() {
        frame.titleTextField.setText("   ");
        frame.artistTextField.setText("   ");
        frame.priceTextField.setText("   ");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testButtonDisabledWhenOnlyArtistMissing() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("");
        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testRemoveEventFromTextFields() {
        frame.titleTextField.setText("Test");
        frame.artistTextField.setText("Test");
        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isTrue();

        frame.titleTextField.setText("");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testShowErrorWithNullTitle() {
        frame.showError("Error", null);
        assertThat(frame.errorLabel.getText()).isEqualTo("Error");
    }
    @Test void testArtworkAddedUpdatesErrorLabel() {
        frame.errorLabel.setText("Previous error");
        Artwork artwork = new Artwork("Test", "Artist", 100.0);
        frame.artworkAdded(artwork);
        assertThat(frame.errorLabel.getText()).isEqualTo(" ");
    }
    @Test void testShowAllArtworksWithMultipleArtworks() {
        Artwork a1 = new Artwork("A1", "Artist1", 100.0);
        Artwork a2 = new Artwork("A2", "Artist2", 200.0);
        Artwork a3 = new Artwork("A3", "Artist3", 300.0);
        frame.showAllArtworks(Arrays.asList(a1, a2, a3));
        assertThat(frame.listModel.size()).isEqualTo(3);
    }
    @Test void testSelectAndRemoveArtwork() {
        Artwork a1 = new Artwork("A1", "Artist1", 100.0);
        Artwork a2 = new Artwork("A2", "Artist2", 200.0);
        frame.artworkAdded(a1);
        frame.artworkAdded(a2);
        frame.artworkRemoved(a1);
        assertThat(frame.listModel.size()).isEqualTo(1);
        assertThat(frame.listModel.get(0)).contains("A2");
    }
    @Test void testAddButtonDisabledWhenPriceContainsText() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("Artist");
        frame.priceTextField.setText("0.0");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testAddButtonWithTrimmedWhitespace() {
        frame.titleTextField.setText("  Title  ");
        frame.artistTextField.setText("  Artist  ");
        frame.priceTextField.setText("  100.0  ");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testFrameInitializesWithCorrectComponents() {
        assertThat(frame.artworkList).isNotNull();
        assertThat(frame.addButton).isNotNull();
        assertThat(frame.deleteButton).isNotNull();
        assertThat(frame.errorLabel).isNotNull();
        assertThat(frame.titleTextField).isNotNull();
        assertThat(frame.artistTextField).isNotNull();
        assertThat(frame.priceTextField).isNotNull();
    }
    @Test void testShowAllArtworksWithSingleArtwork() {
        Artwork a1 = new Artwork("Solo", "Artist", 150.0);
        frame.showAllArtworks(Arrays.asList(a1));
        assertThat(frame.listModel.size()).isEqualTo(1);
        assertThat(frame.listModel.get(0)).contains("Solo");
    }
    @Test void testAddMultipleArtworksSequentially() {
        Artwork a1 = new Artwork("A1", "A1", 100.0);
        Artwork a2 = new Artwork("A2", "A2", 200.0);
        Artwork a3 = new Artwork("A3", "A3", 300.0);
        frame.artworkAdded(a1);
        frame.artworkAdded(a2);
        frame.artworkAdded(a3);
        assertThat(frame.listModel.size()).isEqualTo(3);
    }
    @Test void testErrorLabelInitiallyBlank() {
        assertThat(frame.errorLabel.getText()).isEqualTo(" ");
    }
    @Test void testListSelectionMode() {
        assertThat(frame.artworkList.getSelectionMode()).isZero(); // SINGLE_SELECTION = 0
    }
    @Test void testSetControllerCallsAllArtworks() {
        verify(mockController).allArtworks();
    }
    @Test void testAddButtonInitiallyDisabled() {
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testAllTextFieldsEmpty() {
        assertThat(frame.titleTextField.getText()).isEmpty();
        assertThat(frame.artistTextField.getText()).isEmpty();
        assertThat(frame.priceTextField.getText()).isEmpty();
    }
    @Test void testListModelEmpty() {
        assertThat(frame.listModel.getSize()).isZero();
    }
    @Test void testAddAndRemoveMultiple() {
        Artwork a1 = new Artwork("A1", "Artist1", 100.0);
        Artwork a2 = new Artwork("A2", "Artist2", 200.0);
        Artwork a3 = new Artwork("A3", "Artist3", 300.0);

        frame.artworkAdded(a1);
        frame.artworkAdded(a2);
        frame.artworkAdded(a3);
        assertThat(frame.listModel.getSize()).isEqualTo(3);

        frame.artworkRemoved(a2);
        assertThat(frame.listModel.getSize()).isEqualTo(2);
        assertThat(frame.listModel.get(0)).contains("A1");
        assertThat(frame.listModel.get(1)).contains("A3");
    }
    @Test void testShowAllArtworksClearsExisting() {
        Artwork a1 = new Artwork("A1", "Artist1", 100.0);
        frame.artworkAdded(a1);
        assertThat(frame.listModel.getSize()).isEqualTo(1);

        Artwork a2 = new Artwork("A2", "Artist2", 200.0);
        Artwork a3 = new Artwork("A3", "Artist3", 300.0);
        frame.showAllArtworks(Arrays.asList(a2, a3));
        assertThat(frame.listModel.getSize()).isEqualTo(2);
        assertThat(frame.listModel.get(0)).contains("A2");
        assertThat(frame.listModel.get(1)).contains("A3");
    }
    @Test void testErrorLabelUpdatesCorrectly() {
        frame.errorLabel.setText("Old error");
        Artwork artwork = new Artwork("Test", "Artist", 100.0);
        frame.showError("New error", artwork);
        assertThat(frame.errorLabel.getText()).contains("New error").contains("Test");
    }
    @Test void testButtonStatesWithPartialInput() {
        frame.titleTextField.setText("Title");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.artistTextField.setText("Artist");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testClearAllFields() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("Artist");
        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isTrue();

        Artwork artwork = new Artwork("Title", "Artist", 100.0);
        frame.artworkAdded(artwork);

        assertThat(frame.titleTextField.getText()).isEmpty();
        assertThat(frame.artistTextField.getText()).isEmpty();
        assertThat(frame.priceTextField.getText()).isEmpty();
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testFrameSize() {
        assertThat(frame.getWidth()).isEqualTo(600);
        assertThat(frame.getHeight()).isEqualTo(400);
    }
    @Test void testFrameTitle() {
        assertThat(frame.getTitle()).isEqualTo("Art Gallery Management System");
    }
    @Test void testAddButtonName() {
        assertThat(frame.addButton.getName()).isEqualTo("addButton");
    }
    @Test void testDeleteButtonName() {
        assertThat(frame.deleteButton.getName()).isEqualTo("deleteButton");
    }
    @Test void testTitleTextFieldName() {
        assertThat(frame.titleTextField.getName()).isEqualTo("titleTextField");
    }
    @Test void testArtistTextFieldName() {
        assertThat(frame.artistTextField.getName()).isEqualTo("artistTextField");
    }
    @Test void testPriceTextFieldName() {
        assertThat(frame.priceTextField.getName()).isEqualTo("priceTextField");
    }
    @Test void testArtworkListName() {
        assertThat(frame.artworkList.getName()).isEqualTo("artworkList");
    }
    @Test void testErrorLabelName() {
        assertThat(frame.errorLabel.getName()).isEqualTo("errorLabel");
    }
    @Test void testMultipleArtworksWithSameTitle() {
        Artwork a1 = new Artwork("Same", "Artist1", 100.0);
        Artwork a2 = new Artwork("Same", "Artist2", 200.0);
        frame.artworkAdded(a1);
        frame.artworkAdded(a2);
        assertThat(frame.listModel.getSize()).isEqualTo(2);
    }
    @Test void testRemoveArtworkByContent() {
        Artwork a = new Artwork("Test", "Artist", 100.0);
        frame.artworkAdded(a);
        String item = frame.listModel.get(0);
        assertThat(item).contains(a.getId()).contains("Test");
        frame.artworkRemoved(a);
        assertThat(frame.listModel.getSize()).isZero();
    }
    @Test void testErrorLabelPersistence() {
        frame.showError("Error 1", null);
        assertThat(frame.errorLabel.getText()).contains("Error 1");
        frame.showError("Error 2", null);
        assertThat(frame.errorLabel.getText()).contains("Error 2");
    }
    @Test void testFieldValidationAllEmpty() {
        frame.titleTextField.setText("");
        frame.artistTextField.setText("");
        frame.priceTextField.setText("");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testFieldValidationWhitespaceOnly() {
        frame.titleTextField.setText("   ");
        frame.artistTextField.setText("   ");
        frame.priceTextField.setText("   ");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testFieldValidationMixed() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("   ");
        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
    @Test void testAddButtonActionListener() {
        frame.titleTextField.setText("Test Artwork");
        frame.artistTextField.setText("Test Artist");
        frame.priceTextField.setText("100.0");
        frame.addButton.doClick();
        verify(mockController).addArtwork(any(Artwork.class));
    }
    @Test void testDeleteButtonActionListener() {
        Artwork artwork = new Artwork("Test", "Artist", 100.0);
        frame.artworkAdded(artwork);
        frame.artworkList.setSelectedIndex(0);
        frame.deleteButton.doClick();
        verify(mockController).deleteArtwork(any(String.class));
    }
    @Test void testAddButtonInvalidPrice() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("Artist");
        frame.priceTextField.setText("invalid");
        frame.addButton.setEnabled(true);
        frame.addButton.doClick();
        assertThat(frame.errorLabel.getText()).contains("Invalid price");
    }
    @Test void testDocumentListenerChangedUpdate() {
        frame.titleTextField.setText("Title");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.artistTextField.setText("Artist");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.priceTextField.setText("100.0");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testDeleteButtonNoSelection() {
        frame.artworkList.setSelectedIndex(-1);
        frame.deleteButton.doClick();
        verify(mockController, never()).deleteArtwork(any(String.class));
    }
    @Test void testDocumentListenerInsertUpdatePath() {
        frame.titleTextField.setText("T");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("Artist");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.priceTextField.setText("99.99");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testDocumentListenerRemoveUpdatePath() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("Artist");
        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isTrue();

        frame.titleTextField.setText("");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.titleTextField.setText("Title");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testUpdateAddButtonMultipleChanges() {
        frame.titleTextField.setText("A");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.titleTextField.setText("AB");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.titleTextField.setText("ABC");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.artistTextField.setText("Artist");
        assertThat(frame.addButton.isEnabled()).isFalse();

        frame.priceTextField.setText("50");
        assertThat(frame.addButton.isEnabled()).isTrue();

        frame.priceTextField.setText("50.5");
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testDocumentListenerChangedUpdateMethod() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("Artist");
        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isTrue();

        // Directly invoke changedUpdate through the documentListener field
        DocumentEvent mockEvent = mock(DocumentEvent.class);
        frame.documentListener.changedUpdate(mockEvent);

        // Verify button state is maintained after changedUpdate
        assertThat(frame.addButton.isEnabled()).isTrue();
    }
    @Test void testDocumentListenerChangedUpdateDisablesButton() {
        frame.titleTextField.setText("Title");
        frame.artistTextField.setText("");
        frame.priceTextField.setText("100");
        assertThat(frame.addButton.isEnabled()).isFalse();

        // Invoke changedUpdate - should not change button state since artist is empty
        DocumentEvent mockEvent = mock(DocumentEvent.class);
        frame.documentListener.changedUpdate(mockEvent);

        // Button should still be disabled
        assertThat(frame.addButton.isEnabled()).isFalse();
    }
}
