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
	@Test
	@DisplayName("Should throw exception when title is null")
	void testArtworkNullTitle() {
		assertThatThrownBy(() -> new Artwork(null, "Van Gogh", 1000.0))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Title cannot be null");
	}
	@Test
	@DisplayName("Should throw exception when artist is null")
	void testArtworkNullArtist() {
		assertThatThrownBy(() -> new Artwork("Starry Night", null, 1000.0))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Artist cannot be null");
	}
	@Test
	@DisplayName("Should throw exception when price is negative")
	void testArtworkNegativePrice() {
		assertThatThrownBy(() -> new Artwork("Starry Night", "Van Gogh", -100.0))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Price cannot be negative");
	}
	@Test
	@DisplayName("Should accept zero price")
	void testArtworkZeroPrice() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 0.0);
		assertThat(artwork.getPrice()).isZero();
	}
	@Test
	@DisplayName("Should set year with validation")
	void testSetYearValid() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork.setYear(1889);
		assertThat(artwork.getYear()).isEqualTo(1889);
	}
	@Test
	@DisplayName("Should throw exception when year is too old")
	void testSetYearTooOld() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		assertThatThrownBy(() -> artwork.setYear(999))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Invalid year");
	}