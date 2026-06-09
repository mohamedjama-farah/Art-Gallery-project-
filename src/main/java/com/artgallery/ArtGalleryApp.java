package com.artgallery;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import com.artgallery.controller.ArtworkController;
import com.artgallery.repository.MongoArtworkRepository;
import com.artgallery.view.swing.ArtGalleryFrame;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
public class ArtGalleryApp {
    private static final Logger LOGGER = Logger.getLogger(ArtGalleryApp.class.getName());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> initializeApp());
    }

    static void initializeApp() {
        try {
            MongoClient client = MongoClients.create("mongodb://localhost:27017");
            MongoArtworkRepository repo = new MongoArtworkRepository(client, "artgallery");
            ArtGalleryFrame frame = new ArtGalleryFrame();
            ArtworkController ctrl = new ArtworkController(repo, frame);
            frame.setController(ctrl);
            frame.setVisible(true);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting", e);
        }
    }
}
