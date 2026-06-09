package com.artgallery.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Artwork Model Tests")
class ArtworkTest {
	@Test
	@DisplayName("Should create artwork with valid parameters")
	void testArtworkCreation() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		assertThat(artwork.getTitle()).isEqualTo("Starry Night");
		assertThat(artwork.getArtist()).isEqualTo("Van Gogh");
		assertThat(artwork.getPrice()).isEqualTo(1000.0);
	}