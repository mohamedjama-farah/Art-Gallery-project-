package com.artgallery.view.swing;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.artgallery.controller.ArtworkController;
import com.artgallery.model.Artwork;
import com.artgallery.view.ArtGalleryView;

/**
 * Main GUI frame for the Art Gallery Management System.
 * Provides user interface for viewing, adding, and deleting artwork.
 */
public class ArtGalleryFrame extends JFrame implements ArtGalleryView {
    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 400;
    private transient ArtworkController controller;
    JTextField titleTextField;
    JTextField artistTextField;
    JTextField priceTextField;
    JButton addButton;
    JButton deleteButton;
    JList<String> artworkList;
    DefaultListModel<String> listModel;
    JLabel errorLabel;
    transient DocumentListener documentListener;

    /**
     * Initializes the Art Gallery Frame with all UI components.
     */
    public ArtGalleryFrame() {
        setTitle("Art Gallery Management System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initializes all UI components (buttons, text fields, labels).
     */
    private void initializeComponents() {
        listModel = new DefaultListModel<>();
        artworkList = new JList<>(listModel);
        artworkList.setName("artworkList");
        artworkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        titleTextField = new JTextField(8);
        titleTextField.setName("titleTextField");

        artistTextField = new JTextField(8);
        artistTextField.setName("artistTextField");

        priceTextField = new JTextField(6);
        priceTextField.setName("priceTextField");

        addButton = new JButton("Add");
        addButton.setName("addButton");
        addButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.setName("deleteButton");

        errorLabel = new JLabel(" ");
        errorLabel.setName("errorLabel");
    }

    /**
     * Sets up the layout of the frame with input panel and list.
     */
    private void setupLayout() {
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleTextField);
        inputPanel.add(new JLabel("Artist:"));
        inputPanel.add(artistTextField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceTextField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(artworkList), BorderLayout.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    /**
     * Sets up action listeners for buttons and document listeners for text fields.
     */
    private void setupListeners() {
        addButton.addActionListener(e -> handleAddArtwork());
        deleteButton.addActionListener(e -> handleDeleteArtwork());

        documentListener = new FieldChangeListener();
        titleTextField.getDocument().addDocumentListener(documentListener);
        artistTextField.getDocument().addDocumentListener(documentListener);
        priceTextField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * Handles the add artwork button click event.
     */
    private void handleAddArtwork() {
        try {
            double price = Double.parseDouble(priceTextField.getText());
            Artwork artwork = new Artwork(
                titleTextField.getText(),
                artistTextField.getText(),
                price
            );
            controller.addArtwork(artwork);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid price");
        }
    }

    /**
     * Handles the delete artwork button click event.
     */
    private void handleDeleteArtwork() {
        int idx = artworkList.getSelectedIndex();
        if (idx >= 0) {
            String item = listModel.getElementAt(idx);
            String[] parts = item.split(" - ", 2);
            controller.deleteArtwork(parts[0]);
        }
    }

    /**
     * Document listener for text field updates.
     */
    private class FieldChangeListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateAddButton();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateAddButton();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateAddButton();
        }

        /**
         * Updates the add button enabled state based on field content.
         */
        private void updateAddButton() {
            addButton.setEnabled(
                !titleTextField.getText().trim().isEmpty() &&
                !artistTextField.getText().trim().isEmpty() &&
                !priceTextField.getText().trim().isEmpty());
        }
    }

    /**
     * Sets the controller and loads all artworks.
     * @param controller the artwork controller (non-null)
     */
    public void setController(ArtworkController controller) {
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        this.controller.allArtworks();
    }

    /**
     * Displays all artworks in the list.
     * @param artworks list of artworks to display
     */
    @Override
    public void showAllArtworks(List<Artwork> artworks) {
        listModel.clear();
        for (Artwork a : artworks) {
            listModel.addElement(a.getId() + " - " + a.getTitle());
        }
    }

    /**
     * Adds artwork to the list and clears input fields.
     * @param artwork the artwork that was added
     */
    @Override
    public void artworkAdded(Artwork artwork) {
        listModel.addElement(artwork.getId() + " - " + artwork.getTitle());
        errorLabel.setText(" ");
        titleTextField.setText("");
        artistTextField.setText("");
        priceTextField.setText("");
    }

    /**
     * Removes artwork from the list.
     * @param artwork the artwork that was removed
     */
    @Override
    public void artworkRemoved(Artwork artwork) {
        listModel.removeElement(artwork.getId() + " - " + artwork.getTitle());
        errorLabel.setText(" ");
    }

    /**
     * Displays error message in the error label.
     * @param message the error message
     * @param artwork optional artwork related to the error
     */
    @Override
    public void showError(String message, Artwork artwork) {
        errorLabel.setText(message + (artwork != null ? ": " + artwork.getTitle() : ""));
    }
}
