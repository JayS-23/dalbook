package com.example.facebook_integration.controller;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.model.UserGroup;
import com.example.facebook_integration.repository.GroupRepository;
import com.example.facebook_integration.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @PutMapping("/addUser")
    public UserGroup addUser(@RequestParam int groupId, @RequestParam int userId) {
        return  groupService.addUserToGroup(groupId, userId);
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam int groupId, @RequestParam int userId) {
        return groupService.deleteUser(groupId, userId);
    }

    @PostMapping("/leaveGroup")
    public void leaveGroup(@RequestParam int groupId, @RequestParam int userId) {
        groupService.leaveGroup(groupId, userId);
    }

    @PostMapping("/createGroup")
    public UserGroup createGroup(@RequestBody UserGroup userGroup) {
        return groupService.createGroup(userGroup);
    }

    @GetMapping("/users/{groupId}")
    public List<User> getAllUsers(@PathVariable int groupId) {
        return groupService.getAllUsers(groupId);
    }

    @GetMapping("/get/{groupId}")
    public UserGroup getGroup(@PathVariable int groupId) {
        Optional<UserGroup> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        return group.get();
    }

    @GetMapping("/groups")
    public List<UserGroup> getGroups() {
        return groupRepository.findAll();
    }

    @GetMapping("/checkAdmin/{groupId}/{userId}")
    public ResponseEntity<?> checkIfGroupAdmin(@PathVariable int groupId, @PathVariable int userId) {
        boolean isAdmin = groupService.isGroupAdmin(groupId, userId);
        return ResponseEntity.ok().body(Collections.singletonMap("isAdmin", isAdmin));
    }

    @DeleteMapping("/deleteGroup/{groupId}")
    public String deleteGroup(@PathVariable int groupId) {
        Optional<UserGroup> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        groupRepository.deleteById(groupId);
        return "Group " + group.get().getGroupName() + " Id: "+ group.get().getId()+ " deleted";
    }

    @PutMapping("/join")
    public ResponseEntity<?> joinGroup(@RequestBody Map<String, Integer> request) {
        int groupId = request.get("groupId");
        int userId = request.get("userId");
        groupService.joinGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }
}

