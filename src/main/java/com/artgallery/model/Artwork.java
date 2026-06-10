package com.artgallery.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an artwork in the gallery.
 * Contains information about title, artist, price, year, and description.
 */
public class Artwork {
	private static final int MIN_YEAR = 1000;
	private static final int MAX_YEAR = 2100;

	private String id;
	private String title;
	private String artist;
	private double price;
	private int year;
	private String description;
	private String categoryId;

	/**
	 * Creates a new Artwork with title, artist, and price.
	 * @param title artwork title (non-null)
	 * @param artist artwork artist (non-null)
	 * @param price artwork price (must be non-negative)
	 * @throws IllegalArgumentException if price is negative
	 */
	public Artwork(String title, String artist, double price) {
		this.id = UUID.randomUUID().toString();
		this.title = Objects.requireNonNull(title, "Title cannot be null");
		this.artist = Objects.requireNonNull(artist, "Artist cannot be null");
		if (price < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		this.price = price;
	}

	/**
	 * Returns the unique identifier of this artwork.
	 * @return artwork ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the unique identifier of this artwork.
	 * @param id artwork ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the title of this artwork.
	 * @return artwork title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the artist of this artwork.
	 * @return artwork artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * Returns the price of this artwork.
	 * @return artwork price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Returns the year of this artwork.
	 * @return artwork year (0 if not set)
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets the year of this artwork.
	 * @param year artwork year (must be between 1000 and 2100)
	 * @throws IllegalArgumentException if year is out of valid range
	 */
	public void setYear(int year) {
		if (year < MIN_YEAR || year > MAX_YEAR) {
			throw new IllegalArgumentException("Invalid year");
		}
		this.year = year;
	}

	/**
	 * Returns the description of this artwork.
	 * @return artwork description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this artwork.
	 * @param description artwork description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the category ID of this artwork.
	 * @return category ID, or null if not set
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets the category ID of this artwork.
	 * @param categoryId the category ID
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Returns the hash code of this artwork.
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(artist, description, price, title, year);
	}

	/**
	 * Compares this artwork with another object for equality.
	 * @param obj object to compare
	 * @return true if equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Artwork)) {
			return false;
		}
		Artwork other = (Artwork) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(description, other.description)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(title, other.title) && year == other.year;
	}

	/**
	 * Returns a string representation of this artwork.
	 * @return string representation
	 */
	@Override
	public String toString() {
		return "Artwork [id=" + id + ", title=" + title + ", artist=" + artist + ", price=" + price + ", year=" + year
				+ ", description=" + description + "]";
	}
}
