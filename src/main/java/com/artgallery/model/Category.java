package com.artgallery.model;

import java.util.Objects;

/**
 * Represents a category of artworks in the gallery.
 * Contains information about the category name and description.
 */
public class Category {
	private String id;
	private String name;
	private String description;

	/**
	 * Creates a new Category with the given name.
	 * @param name category name (non-null and non-blank)
	 * @throws IllegalArgumentException if name is blank
	 */
	public Category(String name) {
		this.name = Objects.requireNonNull(name, "Name cannot be null");
		if (name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be blank");
		}
	}

	/**
	 * Returns the unique identifier of this category.
	 * @return category ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the unique identifier of this category.
	 * @param id category ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the name of this category.
	 * @return category name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of this category.
	 * @return category description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this category.
	 * @param description category description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the hash code of this category.
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(description, name);
	}

	/**
	 * Compares this category with another object for equality.
	 * @param obj object to compare
	 * @return true if equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name);
	}

	/**
	 * Returns a string representation of this category.
	 * @return string representation
	 */
	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
}
