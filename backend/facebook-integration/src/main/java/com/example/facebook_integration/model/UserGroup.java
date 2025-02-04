package com.example.facebook_integration.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String groupName;
    private String description;
    private String faculty;
    private int creatorId;

    @ManyToMany(targetEntity = User.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<User>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "group_admins", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "admin_id")
    private Set<Integer> admins = new HashSet<>();

    public UserGroup(){}

    public UserGroup(int id, String groupName, String description, String faculty, int creatorId) {
        this.id = id;
        this.groupName = groupName;
        this.description = description;
        this.faculty = faculty;
        this.creatorId = creatorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        this.groupName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void addAdmin(User user) {
        this.admins.add(user.getId());
    }

    public boolean isAdmin(User user) {
        return this.admins.contains(user.getId());
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }


}
