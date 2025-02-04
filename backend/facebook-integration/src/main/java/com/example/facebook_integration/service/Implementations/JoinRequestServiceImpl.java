package com.example.facebook_integration.service.Implementations;

import com.example.facebook_integration.model.JoinRequest;
import com.example.facebook_integration.model.User;
import com.example.facebook_integration.model.UserGroup;
import com.example.facebook_integration.repository.JoinRequestRepository;
import com.example.facebook_integration.service.GroupService;
import com.example.facebook_integration.service.JoinRequestService;
import com.example.facebook_integration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinRequestServiceImpl implements JoinRequestService {

    @Autowired
    private JoinRequestRepository joinRequestRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    public List<JoinRequest> getJoinRequestsByGroup(int groupId) {
        return joinRequestRepository.findByGroupId(groupId);
    }

    public void approveRequest(int requestId) {
        JoinRequest request = joinRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("APPROVED");

        // Fetch the group and user
        UserGroup group = groupService.getGroupById(request.getGroupId()).orElseThrow(() ->
                new IllegalArgumentException("Group cannot be found"));
        User user = userService.findUserById(request.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("User cannot be found"));

        // Add user to the group
        groupService.addUserToGroup(group.getId(), user.getId());

        // Save the updated join request
        joinRequestRepository.save(request);
    }

    public void rejectRequest(int requestId) {
        JoinRequest request = joinRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("REJECTED");
        joinRequestRepository.save(request);
    }

    public void createRequest(int userId, int groupId) {
        JoinRequest request = new JoinRequest();
        request.setUserId(userId);
        request.setGroupId(groupId);
        request.setStatus("PENDING");
        joinRequestRepository.save(request);
    }

    public List<JoinRequest> getApprovedRequests() {
        return joinRequestRepository.findByStatus("APPROVED");
    }
}
