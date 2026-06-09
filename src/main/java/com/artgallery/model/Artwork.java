package com.artgallery.model;

import java.util.Objects;
import java.util.UUID;

public class Artwork {
	private String id;
	private String title;
	private String artist;
	private double price;
	private int year;
	private String description;

	public Artwork(String title, String artist, double price) {
		this.id = UUID.randomUUID().toString();
		this.title = Objects.requireNonNull(title, "Title cannot be null");
		this.artist = Objects.requireNonNull(artist, "Artist cannot be null");
		if (price < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public double getPrice() {
		return price;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		if (year < 1000 || year > 2100) {
			throw new IllegalArgumentException("Invalid year");
		}
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artist, description, price, title, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artwork other = (Artwork) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(description, other.description)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(title, other.title) && year == other.year;
	}

	@Override
	public String toString() {
		return "Artwork [id=" + id + ", title=" + title + ", artist=" + artist + ", price=" + price + ", year=" + year
				+ ", description=" + description + "]";
	}
}
