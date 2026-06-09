package com.artgallery.e2e;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.mockito.Mockito.mock;
import com.artgallery.controller.ArtworkController;
import com.artgallery.view.ArtGalleryView;
import com.artgallery.controller.CategoryController;
import com.artgallery.model.Artwork;
import com.artgallery.model.Category;
import com.artgallery.repository.MongoArtworkRepository;
import com.artgallery.repository.MongoCategoryRepository;
import com.artgallery.view.ArtGalleryView;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
@Testcontainers
class ArtGalleryE2EIT {
    @Container static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0");
    private ArtworkController artworkController;
    private CategoryController categoryController;
    @BeforeAll static void setupContainer() { mongoContainer.start(); }
    @BeforeEach void setUp() {
        MongoClient mongoClient = MongoClients.create(mongoContainer.getReplicaSetUrl());
        MongoArtworkRepository artworkRepository = new MongoArtworkRepository(mongoClient, "artgallery_e2e");
        MongoCategoryRepository categoryRepository = new MongoCategoryRepository(mongoClient, "artgallery_e2e");
        ArtGalleryView dummyView = mock(ArtGalleryView.class);
        artworkController = new ArtworkController(artworkRepository, dummyView);
        categoryController = new CategoryController(categoryRepository);
        artworkController.getAllArtworks().forEach(a -> artworkController.deleteArtwork(a.getId()));
        categoryController.getAllCategories().forEach(c -> categoryController.deleteCategory(c.getId()));
    }
    @Test void testAddAndRetrieveArtwork() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        artwork.setYear(1889);
        artworkController.addArtwork(artwork);
        Artwork retrieved = artworkController.getArtworkById(artwork.getId());
        assertThat(retrieved.getTitle()).isEqualTo("Starry Night");
        assertThat(retrieved.getPrice()).isEqualTo(1000.0);
        assertThat(retrieved.getYear()).isEqualTo(1889);
    }
    @Test void testListAllArtworks() {
        artworkController.addArtwork(new Artwork("Starry Night", "Van Gogh", 1000.0));
        artworkController.addArtwork(new Artwork("The Kiss", "Klimt", 2000.0));
        assertThat(artworkController.getAllArtworks()).hasSize(2);
    }
    @Test void testDeleteArtwork() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        artworkController.addArtwork(artwork);
        artworkController.deleteArtwork(artwork.getId());
        assertThat(artworkController.getAllArtworks()).isEmpty();
    }
    @Test void testUpdateArtwork() {
        Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
        artworkController.addArtwork(artwork);
        artwork.setDescription("Updated");
        artworkController.updateArtwork(artwork);
        assertThat(artworkController.getArtworkById(artwork.getId()).getDescription()).isEqualTo("Updated");
    }
    @Test void testCompleteArtworkWorkflow() {
        Artwork a1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
        Artwork a2 = new Artwork("Persistence", "Dali", 2000.0);
        Artwork a3 = new Artwork("The Kiss", "Klimt", 3000.0);
        artworkController.addArtwork(a1);
        artworkController.addArtwork(a2);
        artworkController.addArtwork(a3);
        assertThat(artworkController.getAllArtworks()).hasSize(3);
        artworkController.deleteArtwork(a2.getId());
        assertThat(artworkController.getAllArtworks()).hasSize(2);
    }
    @Test void testAddAndRetrieveCategory() {
        Category category = new Category("Painting");
        category.setDescription("Oil paintings");
        categoryController.addCategory(category);
        Category retrieved = categoryController.getCategoryById(category.getId());
        assertThat(retrieved.getName()).isEqualTo("Painting");
    }
    @Test void testCompleteCategoryWorkflow() {
        Category c1 = new Category("Modern Art");
        Category c2 = new Category("Classical Art");
        categoryController.addCategory(c1);
        categoryController.addCategory(c2);
        assertThat(categoryController.getAllCategories()).hasSize(2);
        categoryController.deleteCategory(c2.getId());
        assertThat(categoryController.getAllCategories()).hasSize(1);
    }
}
