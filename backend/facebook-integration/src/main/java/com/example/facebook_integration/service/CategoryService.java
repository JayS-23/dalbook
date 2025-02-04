package com.example.facebook_integration.service;

import com.example.facebook_integration.model.CategoryOfDay;

import java.util.List;

public interface CategoryService {
    CategoryOfDay saveCategory(CategoryOfDay category);
    List<CategoryOfDay> getAllCategories();
    List<CategoryOfDay> getPostsByTopic(String topic);
}
