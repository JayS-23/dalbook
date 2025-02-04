package com.example.facebook_integration.controller;

import com.example.facebook_integration.model.CategoryOfDay;
import com.example.facebook_integration.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/posts")
    public ResponseEntity<CategoryOfDay> createPost(@RequestBody CategoryOfDay category) {

        return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryOfDay>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/getPosts")
    public ResponseEntity<List<CategoryOfDay>> getPostsByTopic(@RequestParam String topic) {
        return new ResponseEntity<>(categoryService.getPostsByTopic(topic), HttpStatus.OK);
    }
}
