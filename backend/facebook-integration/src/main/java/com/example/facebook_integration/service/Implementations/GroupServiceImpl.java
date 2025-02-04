package com.example.facebook_integration.service.Implementations;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.model.UserGroup;
import com.example.facebook_integration.repository.GroupRepository;
import com.example.facebook_integration.repository.UserRepository;
import com.example.facebook_integration.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserGroup createGroup(UserGroup userGroup) {
        if (userGroup == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }
        if (groupRepository.existsById(userGroup.getId())) {
            throw new IllegalArgumentException("Group already exists");
        }
        UserGroup savedUserGroup = groupRepository.save(userGroup);

        // Assign Group_Admin role to the user who created the group
        User creator = userRepository.findById(userGroup.getCreatorId()).orElseThrow(() ->
                new IllegalArgumentException("User cannot be found"));
        creator.getGroups().add(savedUserGroup);
        savedUserGroup.addUser(creator);
        userRepository.save(creator);

        savedUserGroup.addAdmin(creator);
        groupRepository.save(savedUserGroup);

        return savedUserGroup;
    }

    @Override
    public UserGroup addUserToGroup(int groupId, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User cannot be found"));
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("Group cannot be found"));

        if (userGroup.getUsers().contains(user)) {
            throw new IllegalArgumentException("User already exists in the group");
        }

        user.getGroups().add(userGroup);
        userGroup.addUser(user);
        userRepository.save(user);
        return groupRepository.save(userGroup);
    }

    @Override
    public String deleteGroup(int groupId, int userId) {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("Group cannot be found"));

        // Check if the requester has Group_Admin role
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User cannot be found"));

        if (!userGroup.isAdmin(requester)) {
            throw new SecurityException("User does not have Group_Admin role");
        }

        groupRepository.delete(userGroup);
        return "Group successfully deleted";
    }

    @Override
    public String deleteUser(int groupId, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User cannot be found"));
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("Group cannot be found"));

        if (!userGroup.hasUser(user)) {
            throw new IllegalArgumentException("User is not in this group");
        }

        userGroup.removeUser(user);
        groupRepository.save(userGroup);
        return "User successfully removed";
    }

    @Override
    public List<User> getAllUsers(int groupId) {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("Group cannot be found"));
        return userGroup.getUsers();
    }

    @Override
    public boolean isGroupAdmin(int groupId, int userId) {
        UserGroup userGroup = groupRepository.findById(groupId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (userGroup != null && user != null) {
            return userGroup.isAdmin(user);
        }
        return false;
    }

    @Override
    public void joinGroup(int groupId, int userId) {
        UserGroup group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        group.getUsers().add(user);
        groupRepository.save(group);
    }

    @Override
    public void leaveGroup(int groupId, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User cannot be found"));
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("Group cannot be found"));

        if (!userGroup.hasUser(user)) {
            throw new IllegalArgumentException("User is not in this group");
        }

        userGroup.removeUser(user);
        groupRepository.save(userGroup);
    }

    @Override
    public Optional<UserGroup> getGroupById(int groupId) {
        return groupRepository.findById(groupId);
    }
}
