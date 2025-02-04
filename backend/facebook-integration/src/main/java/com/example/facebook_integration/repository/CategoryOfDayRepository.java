package com.example.facebook_integration.repository;

import com.example.facebook_integration.model.CategoryOfDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryOfDayRepository extends JpaRepository<CategoryOfDay, Integer> {
    List<CategoryOfDay> findByTopic(String topic);
}
