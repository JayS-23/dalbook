package com.example.facebook_integration.service.Implementations;

import com.example.facebook_integration.model.CategoryOfDay;
import com.example.facebook_integration.repository.CategoryOfDayRepository;
import com.example.facebook_integration.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryOfDayRepository categoryRepository;

    @Override
    public CategoryOfDay saveCategory(CategoryOfDay category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<CategoryOfDay> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryOfDay> getPostsByTopic(String topic) {
        return categoryRepository.findByTopic(topic);
    }
}
