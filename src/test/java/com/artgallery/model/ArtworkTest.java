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
	@Test
	@DisplayName("Should throw exception when year is too recent")
	void testSetYearTooRecent() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		assertThatThrownBy(() -> artwork.setYear(2101))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Invalid year");
	}
	@Test
	@DisplayName("Should set description")
	void testSetDescription() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork.setDescription("A painting of a starry night");
		assertThat(artwork.getDescription()).isEqualTo("A painting of a starry night");
	}
	@Test
	@DisplayName("Should generate unique ID")
	void testSetAndGetId() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		String id = "507f1f77bcf86cd799439011";
		artwork.setId(id);
		assertThat(artwork.getId()).isEqualTo(id);
	}
	@Test
	@DisplayName("Should be equal when all fields match")
	void testEquality() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork1.setYear(1889);
		Artwork artwork2 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork2.setYear(1889);
		assertThat(artwork1).isEqualTo(artwork2);
	}
	@Test
	@DisplayName("Should not be equal when fields differ")
	void testInequality() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		Artwork artwork2 = new Artwork("Starry Night", "Picasso", 1000.0);
		assertThat(artwork1).isNotEqualTo(artwork2);
	}
	@Test
	@DisplayName("Should handle hashCode correctly")
	void testHashCode() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork1.setYear(1889);
		Artwork artwork2 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork2.setYear(1889);
		assertThat(artwork1.hashCode()).isEqualTo(artwork2.hashCode());
	}
	@Test
	@DisplayName("Should not be equal to null")
	void testEqualityWithNull() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		assertThat(artwork).isNotEqualTo(null);
	}
	@Test
	@DisplayName("Should not be equal to different type")
	void testEqualityWithDifferentType() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		assertThat(artwork).isNotEqualTo("Not an artwork");
	}
	@Test
	@DisplayName("Should not be equal when description differs")
	void testInequalityWithDifferentDescription() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork1.setDescription("Desc1");
		Artwork artwork2 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork2.setDescription("Desc2");
		assertThat(artwork1).isNotEqualTo(artwork2);
	}
	@Test
	@DisplayName("Should not be equal when price differs")
	void testInequalityWithDifferentPrice() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		Artwork artwork2 = new Artwork("Starry Night", "Van Gogh", 2000.0);
		assertThat(artwork1).isNotEqualTo(artwork2);
	}
	@Test
	@DisplayName("Should not be equal when year differs")
	void testInequalityWithDifferentYear() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork1.setYear(1889);
		Artwork artwork2 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		artwork2.setYear(1890);
		assertThat(artwork1).isNotEqualTo(artwork2);
	}
	@Test
	@DisplayName("Should not be equal when title differs")
	void testInequalityWithDifferentTitle() {
		Artwork artwork1 = new Artwork("Starry Night", "Van Gogh", 1000.0);
		Artwork artwork2 = new Artwork("The Kiss", "Van Gogh", 1000.0);
		assertThat(artwork1).isNotEqualTo(artwork2);
	}
	@Test
	@DisplayName("Should have toString representation")
	void testToString() {
		Artwork artwork = new Artwork("Starry Night", "Van Gogh", 1000.0);
		String str = artwork.toString();
		assertThat(str).contains("Starry Night").contains("Van Gogh").contains("1000.0");
	}
	@Test
	@DisplayName("Should have different hashCode for different objects")
	void testHashCodeDifference() {
		Artwork artwork1 = new Artwork("A1", "Artist1", 100.0);
		Artwork artwork2 = new Artwork("A2", "Artist2", 200.0);
		assertThat(artwork1.hashCode()).isNotEqualTo(artwork2.hashCode());
	}
	@Test
	@DisplayName("Should compare with self")
	void testEqualityWithSelf() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		assertThat(artwork).isEqualTo(artwork);
	}
	@Test
	@DisplayName("Should handle boundary year values")
	void testBoundaryYears() {
		Artwork artwork = new Artwork("Test", "Artist", 100.0);
		artwork.setYear(1000);
		assertThat(artwork.getYear()).isEqualTo(1000);
		artwork.setYear(2100);
		assertThat(artwork.getYear()).isEqualTo(2100);
	}
	@Test
	@DisplayName("Should be equal when description is null")
	void testEqualityBothWithNullDescription() {
		Artwork artwork1 = new Artwork("Test", "Artist", 100.0);
		Artwork artwork2 = new Artwork("Test", "Artist", 100.0);
		assertThat(artwork1).isEqualTo(artwork2);
	}
	@Test
	@DisplayName("Should have unique IDs")
	void testUniqueIds() {
		Artwork artwork1 = new Artwork("Test", "Artist", 100.0);
		Artwork artwork2 = new Artwork("Test", "Artist", 100.0);
		assertThat(artwork1.getId()).isNotEqualTo(artwork2.getId());
	}
	@Test
	@DisplayName("Should preserve zero price")
	void testZeroPricePreserved() {
		Artwork artwork = new Artwork("Test", "Artist", 0.0);
		assertThat(artwork.getPrice()).isZero();
	}
	@Test
	@DisplayName("Should have same hash for equal objects")
	void testHashCodeConsistency() {
		Artwork artwork1 = new Artwork("Test", "Artist", 100.0);
		artwork1.setYear(2000);
		artwork1.setDescription("Desc");
		Artwork artwork2 = new Artwork("Test", "Artist", 100.0);
		artwork2.setYear(2000);
		artwork2.setDescription("Desc");
		assertThat(artwork1.hashCode()).isEqualTo(artwork2.hashCode());
	}
}
