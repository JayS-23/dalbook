package com.example.facebook_integration.IntegrationTests;

import com.example.facebook_integration.controller.CategoryController;
import com.example.facebook_integration.model.CategoryOfDay;
import com.example.facebook_integration.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private CategoryOfDay category;

    @BeforeEach
    public void setup() {
        category = new CategoryOfDay();
        category.setId(1);
        category.setUserId(1);
        category.setTopic("Test Topic");
        category.setContent("Test Content");
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreatePost() throws Exception {
        when(categoryService.saveCategory(any(CategoryOfDay.class))).thenReturn(category);

        mockMvc.perform(post("/api/category/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.userId").value(category.getUserId()))
                .andExpect(jsonPath("$.topic").value(category.getTopic()))
                .andExpect(jsonPath("$.content").value(category.getContent()));

        Mockito.verify(categoryService, Mockito.times(1)).saveCategory(any(CategoryOfDay.class));
    }

    @Test
    public void testGetAllCategories() throws Exception {
        List<CategoryOfDay> categories = Arrays.asList(category);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(category.getId()))
                .andExpect(jsonPath("$[0].userId").value(category.getUserId()))
                .andExpect(jsonPath("$[0].topic").value(category.getTopic()))
                .andExpect(jsonPath("$[0].content").value(category.getContent()));

        Mockito.verify(categoryService, Mockito.times(1)).getAllCategories();
    }

    @Test
    public void testGetPostsByTopic() throws Exception {
        List<CategoryOfDay> categories = Arrays.asList(category);
        when(categoryService.getPostsByTopic("Test Topic")).thenReturn(categories);

        mockMvc.perform(get("/api/category/getPosts")
                        .param("topic", "Test Topic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(category.getId()))
                .andExpect(jsonPath("$[0].userId").value(category.getUserId()))
                .andExpect(jsonPath("$[0].topic").value(category.getTopic()))
                .andExpect(jsonPath("$[0].content").value(category.getContent()));

        Mockito.verify(categoryService, Mockito.times(1)).getPostsByTopic("Test Topic");
    }
    @Test
    public void testGetPostsByTopic_NoCategoriesFound() throws Exception {
        when(categoryService.getPostsByTopic("Nonexistent Topic")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/category/getPosts")
                        .param("topic", "Nonexistent Topic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        Mockito.verify(categoryService, Mockito.times(1)).getPostsByTopic("Nonexistent Topic");
    }
}
