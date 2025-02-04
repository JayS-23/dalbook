package com.example.facebook_integration.repository;

import com.example.facebook_integration.model.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Integer> {

    List<JoinRequest> findByGroupIdAndStatus(int groupId, String status);

    List<JoinRequest> findByGroupId(int groupId);

    List<JoinRequest> findByUserId(int userId);

    List<JoinRequest> findByStatus(String status);
}

