package com.example.facebook_integration.service;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.model.UserGroup;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    public String deleteUser(int GroupId,  int userId);
    public UserGroup addUserToGroup(int GroupId, int UserId);
    public UserGroup createGroup(UserGroup userGroup);
    public String deleteGroup( int groupId, int userId);
    public List<User> getAllUsers(int groupId);
    boolean isGroupAdmin(int groupId, int userId);
    void joinGroup(int groupId, int userId);
    void leaveGroup(int groupId, int userId);
    Optional<UserGroup> getGroupById(int groupId);
}

