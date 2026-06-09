package com.artgallery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArtGalleryApp Tests")
public class ArtGalleryAppTest {
    @Test
    @DisplayName("Should have main method")
    void testMainMethodExists() {
        try {
            Class<?> appClass = Class.forName("com.artgallery.ArtGalleryApp");
            assertThat(appClass.getMethod("main", String[].class)).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException("ArtGalleryApp.main method not found", e);
        }
    }
    @Test
    @DisplayName("Should have initializeApp method")
    void testInitializeAppMethodExists() {
        try {
            Class<?> appClass = Class.forName("com.artgallery.ArtGalleryApp");
            assertThat(appClass.getDeclaredMethod("initializeApp")).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException("ArtGalleryApp.initializeApp method not found", e);
        }
    }
    @Test
    @DisplayName("Application class is properly defined")
    void testApplicationClassDefined() {
        try {
            Class<?> appClass = ArtGalleryApp.class;
            assertThat(appClass).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException("ArtGalleryApp class error", e);
        }
    }
}
