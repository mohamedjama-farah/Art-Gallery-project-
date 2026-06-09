package com.artgallery.view.swing;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.artgallery.controller.ArtworkController;
import com.artgallery.model.Artwork;
import com.artgallery.view.ArtGalleryView;
public class ArtGalleryFrame extends JFrame implements ArtGalleryView {
    private static final long serialVersionUID = 1L;
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
    public ArtGalleryFrame() {
        setTitle("Art Gallery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
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
        addButton.addActionListener(e -> {
            try {
                double price = Double.parseDouble(priceTextField.getText());
                controller.addArtwork(new Artwork(titleTextField.getText(), artistTextField.getText(), price));
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid price");
            }
        });
        deleteButton.addActionListener(e -> {
            int idx = artworkList.getSelectedIndex();
            if (idx >= 0) {
                controller.deleteArtwork(listModel.getElementAt(idx).split(" - ")[0]);
            }
        });
        documentListener = new FieldChangeListener();
        titleTextField.getDocument().addDocumentListener(documentListener);
        artistTextField.getDocument().addDocumentListener(documentListener);
        priceTextField.getDocument().addDocumentListener(documentListener);
    }

    private class FieldChangeListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) { updateAddButton(); }
        public void removeUpdate(DocumentEvent e) { updateAddButton(); }
        public void changedUpdate(DocumentEvent e) { updateAddButton(); }
    }
    private void updateAddButton() {
        addButton.setEnabled(
            !titleTextField.getText().trim().isEmpty() &&
            !artistTextField.getText().trim().isEmpty() &&
            !priceTextField.getText().trim().isEmpty());
    }
    public void setController(ArtworkController controller) {
        this.controller = controller;
        controller.allArtworks();
    }
    @Override public void showAllArtworks(List<Artwork> artworks) {
        listModel.clear();
        for (Artwork a : artworks) listModel.addElement(a.getId() + " - " + a.getTitle());
    }
    @Override public void artworkAdded(Artwork artwork) {
        listModel.addElement(artwork.getId() + " - " + artwork.getTitle());
        errorLabel.setText(" ");
        titleTextField.setText(""); artistTextField.setText(""); priceTextField.setText("");
    }
    @Override public void artworkRemoved(Artwork artwork) {
        listModel.removeElement(artwork.getId() + " - " + artwork.getTitle());
        errorLabel.setText(" ");
    }
    @Override public void showError(String message, Artwork artwork) {
        errorLabel.setText(message + (artwork != null ? ": " + artwork.getTitle() : ""));
    }
}
