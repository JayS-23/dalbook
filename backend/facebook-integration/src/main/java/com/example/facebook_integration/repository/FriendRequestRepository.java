package com.example.facebook_integration.repository;

import com.example.facebook_integration.model.FriendRequest;
import com.example.facebook_integration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    List<FriendRequest> findByReceiverAndAcceptedFalse(User receiver);
    List<FriendRequest> findBySenderAndAcceptedFalse(User sender);
    List<FriendRequest> findBySenderAndAcceptedTrue(User sender);
    List<FriendRequest> findByReceiverAndAcceptedTrue(User receiver);
    List<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
}
