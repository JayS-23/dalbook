package com.example.facebook_integration.UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.facebook_integration.model.CategoryOfDay;
import com.example.facebook_integration.repository.CategoryOfDayRepository;
import com.example.facebook_integration.service.Implementations.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CategoryServiceTest {

    @Mock
    private CategoryOfDayRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryOfDay category1;
    private CategoryOfDay category2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        category1 = new CategoryOfDay();
        category1.setId(1);
        category1.setTopic("Technology");

        category2 = new CategoryOfDay();
        category2.setId(2);
        category2.setTopic("Health");
    }

    @Test
    public void testSaveCategory() {
        when(categoryRepository.save(category1)).thenReturn(category1);

        CategoryOfDay result = categoryService.saveCategory(category1);

        assertEquals(category1, result);
        verify(categoryRepository, times(1)).save(category1);
    }

    @Test
    public void testGetAllCategories() {
        List<CategoryOfDay> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryOfDay> result = categoryService.getAllCategories();

        assertEquals(categories, result);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCategoriesEmpty() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryOfDay> result = categoryService.getAllCategories();

        assertEquals(Collections.emptyList(), result);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetPostsByTopic() {
        List<CategoryOfDay> categories = Arrays.asList(category1);
        when(categoryRepository.findByTopic("Technology")).thenReturn(categories);

        List<CategoryOfDay> result = categoryService.getPostsByTopic("Technology");

        assertEquals(categories, result);
        verify(categoryRepository, times(1)).findByTopic("Technology");
    }

    @Test
    public void testGetPostsByTopicEmpty() {
        when(categoryRepository.findByTopic("Nonexistent Topic")).thenReturn(Collections.emptyList());

        List<CategoryOfDay> result = categoryService.getPostsByTopic("Nonexistent Topic");

        assertEquals(Collections.emptyList(), result);
        verify(categoryRepository, times(1)).findByTopic("Nonexistent Topic");
    }
}

