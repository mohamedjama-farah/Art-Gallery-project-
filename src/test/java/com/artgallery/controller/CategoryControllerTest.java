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