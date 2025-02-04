package com.example.facebook_integration.repository;

import com.example.facebook_integration.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<UserGroup, Integer> {
}
