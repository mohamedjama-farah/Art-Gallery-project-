package com.artgallery.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.artgallery.model.Category;
import com.artgallery.repository.CategoryRepository;

class CategoryControllerTest {

    private CategoryRepository mockRepository;
    private CategoryController controller;

    @BeforeEach
    void setUp() {
        mockRepository = mock(CategoryRepository.class);
        controller = new CategoryController(mockRepository);
    }
    @Test
    void testConstructorNullRepository() {
        assertThatThrownBy(() -> new CategoryController(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Repository cannot be null");
    }
    @Test
    void testAddCategory() {
        Category category = new Category("Painting");
        controller.addCategory(category);
        verify(mockRepository).save(category);
    }
    @Test
    void testAddNullCategoryThrowsException() {
        assertThatThrownBy(() -> controller.addCategory(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Category cannot be null");
    }
    @Test
    void testGetCategoryById() {
        Category category = new Category("Painting");
        when(mockRepository.findById("1")).thenReturn(Optional.of(category));

        Category result = controller.getCategoryById("1");
        verify(mockRepository).findById("1");
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Painting");
    }
    @Test
    void testGetCategoryByIdNotFound() {
        when(mockRepository.findById("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.getCategoryById("999"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Category not found: 999");
    }
    @Test
    void testGetAllCategories() {
        List<Category> cats = Arrays.asList(new Category("Painting"), new Category("Sculpture"));
        when(mockRepository.findAll()).thenReturn(cats);
        List<Category> result = controller.getAllCategories();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Painting");
        assertThat(result.get(1).getName()).isEqualTo("Sculpture");
        verify(mockRepository).findAll();
    }
    @Test
    void testDeleteCategory() {
        Category category = new Category("Painting");
        when(mockRepository.findById("1")).thenReturn(Optional.of(category));

        controller.deleteCategory("1");
        verify(mockRepository).delete("1");
    }
    @Test
    void testDeleteCategoryNotFound() {
        when(mockRepository.findById("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.deleteCategory("999"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Category not found: 999");
    }
    @Test
    void testDeleteNullIdThrowsException() {
        assertThatThrownBy(() -> controller.deleteCategory(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("ID cannot be null");
    }
    @Test
    void testUpdateCategory() {
        Category category = new Category("Painting");
        category.setId("1");
        when(mockRepository.findById("1")).thenReturn(Optional.of(category));

        controller.updateCategory(category);
        verify(mockRepository).update(category);
    }