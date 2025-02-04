package com.example.facebook_integration.service;

import com.example.facebook_integration.model.JoinRequest;
import java.util.List;

public interface JoinRequestService {
    List<JoinRequest> getJoinRequestsByGroup(int groupId);
    void approveRequest(int requestId);
    void rejectRequest(int requestId);
    void createRequest(int userId, int groupId);
    List<JoinRequest> getApprovedRequests();
}
