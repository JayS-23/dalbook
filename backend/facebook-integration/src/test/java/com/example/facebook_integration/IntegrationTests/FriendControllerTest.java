package com.example.facebook_integration.IntegrationTests;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.facebook_integration.controller.FriendController;
import com.example.facebook_integration.model.Friend;
import com.example.facebook_integration.model.User;
import com.example.facebook_integration.service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(value = FriendController.class)
public class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    private User user;
    private User friend;
    private Friend friendship;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1);

        friend = new User();
        friend.setId(2);

        friendship = new Friend(user, friend);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFollowUser() throws Exception {
        when(friendService.followUser(anyInt(), anyInt())).thenReturn(friendship);

        mockMvc.perform(post("/api/friends/{userId}/follow/{friendId}", user.getId(), friend.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(user.getId()))
                .andExpect(jsonPath("$.friend.id").value(friend.getId()));

        verify(friendService, times(1)).followUser(user.getId(), friend.getId());
    }

    @Test
    public void testFollowUser_UserNotFound() throws Exception {
        when(friendService.followUser(anyInt(), anyInt())).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/api/friends/{userId}/follow/{friendId}", user.getId(), friend.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(friendService, times(1)).followUser(user.getId(), friend.getId());
    }

    @Test
    public void testUnfollowUser() throws Exception {
        int userId = 1;
        int friendId = 2;

        mockMvc.perform(post("/api/friends/{userId}/unfollow/{friendId}", userId, friendId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(friendService, times(1)).unfollowUser(userId, friendId);
    }

    @Test
    public void testBlockUser() throws Exception {
        int userId = 1;
        int blockedId = 2;
        Friend friendship = new Friend(user, friend);
        friendship.setBlocked(true);

        when(friendService.blockUser(userId, blockedId)).thenReturn(friendship);

        mockMvc.perform(post("/api/friends/{userId}/block/{blockedId}", userId, blockedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(userId))
                .andExpect(jsonPath("$.friend.id").value(blockedId))
                .andExpect(jsonPath("$.blocked").value(true));

        verify(friendService, times(1)).blockUser(userId, blockedId);
    }
    @Test
    public void testUnblockUser() throws Exception {
        int userId = 1;
        int blockedId = 2;
        Friend friendship = new Friend(user, friend);
        friendship.setBlocked(false);

        when(friendService.unblockUser(userId, blockedId)).thenReturn(friendship);

        mockMvc.perform(post("/api/friends/{userId}/unblock/{blockedId}", userId, blockedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(userId))
                .andExpect(jsonPath("$.friend.id").value(blockedId))
                .andExpect(jsonPath("$.blocked").value(false));

        verify(friendService, times(1)).unblockUser(userId, blockedId);
    }
    @Test
    public void testGetFriends() throws Exception {

        int userId = 1;
        List<Friend> friends = Arrays.asList(new Friend(user,friend));
        when(friendService.getFriends(userId)).thenReturn(friends);

        mockMvc.perform(get("/api/friends/{userId}/friends", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.id").value(userId))
                .andExpect(jsonPath("$[0].friend.id").value(2));

        verify(friendService, times(1)).getFriends(userId);
    }
    @Test
    public void testGetBlockedUsers() throws Exception {
        int userId = 1;
        Friend blockedFriend = new Friend(user, friend);
        blockedFriend.setBlocked(true);
        List<Friend> blockedUsers = Arrays.asList(blockedFriend);

        when(friendService.getBlockedUsers(userId)).thenReturn(blockedUsers);

        mockMvc.perform(get("/api/friends/{userId}/blocked", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.id").value(userId))
                .andExpect(jsonPath("$[0].friend.id").value(2))
                .andExpect(jsonPath("$[0].blocked").value(true));

        verify(friendService, times(1)).getBlockedUsers(userId);
    }

}
