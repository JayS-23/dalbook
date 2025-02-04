package com.example.facebook_integration.UnitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.example.facebook_integration.model.Friend;
import com.example.facebook_integration.model.FriendId;
import com.example.facebook_integration.model.User;
import com.example.facebook_integration.repository.FriendRepository;
import com.example.facebook_integration.repository.UserRepository;
import com.example.facebook_integration.service.Implementations.FriendServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendServiceImpl friendService;

    private User user;
    private User friend;
    private Friend friendship;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        friend = new User();
        friend.setId(2);

        friendship = new Friend(user, friend);
    }

    @Test
    public void testFollowUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(friend.getId())).thenReturn(Optional.of(friend));
        when(friendRepository.save(any(Friend.class))).thenReturn(friendship);

        Friend result = friendService.followUser(user.getId(), friend.getId());

        assertEquals(friendship, result);
        verify(friendRepository, times(1)).save(any(Friend.class));
    }
    @Test
    public void testFollowUserFriendNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(friend.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.followUser(user.getId(), friend.getId());
        });

        assertEquals("No value present", exception.getMessage());
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    public void testUnfollowUser() {
        FriendId friendIdObj = new FriendId(user.getId(), friend.getId());

        friendService.unfollowUser(user.getId(), friend.getId());

        verify(friendRepository, times(1)).deleteById(friendIdObj);
    }

    @Test
    public void testBlockUser() {
        FriendId friendIdObj = new FriendId(user.getId(), friend.getId());
        when(friendRepository.findById(friendIdObj)).thenReturn(Optional.of(friendship));
        when(friendRepository.save(any(Friend.class))).thenReturn(friendship);

        Friend result = friendService.blockUser(user.getId(), friend.getId());

        assertTrue(result.isBlocked());
        verify(friendRepository, times(1)).save(friendship);
    }

    @Test
    public void testUnblockUser() {
        FriendId friendIdObj = new FriendId(user.getId(), friend.getId());
        friendship.setBlocked(true);
        when(friendRepository.findById(friendIdObj)).thenReturn(Optional.of(friendship));
        when(friendRepository.save(any(Friend.class))).thenReturn(friendship);

        Friend result = friendService.unblockUser(user.getId(), friend.getId());

        assertFalse(result.isBlocked());
        verify(friendRepository, times(1)).save(friendship);
    }

    @Test
    public void testGetFriends() {
        List<Friend> friends = new ArrayList<>();
        friends.add(friendship);
        when(friendRepository.findByUserId(user.getId())).thenReturn(friends);

        List<Friend> result = friendService.getFriends(user.getId());

        assertEquals(friends, result);
    }
    @Test
    public void testGetFriendsNoFriends() {
        when(friendRepository.findByUserId(user.getId())).thenReturn(Collections.emptyList());

        List<Friend> result = friendService.getFriends(user.getId());

        assertTrue(result.isEmpty());
        verify(friendRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    public void testGetBlockedUsers() {
        List<Friend> friends = new ArrayList<>();
        friendship.setBlocked(true);
        friends.add(friendship);
        when(friendRepository.findByUserId(user.getId())).thenReturn(friends);

        List<Friend> result = friendService.getBlockedUsers(user.getId());

        assertEquals(friends, result);
    }
    @Test
    public void testGetBlockedUsersNoBlockedFriends() {
        List<Friend> friends = new ArrayList<>();
        Friend nonBlockedFriend = new Friend(user, friend);
        nonBlockedFriend.setBlocked(false);
        friends.add(nonBlockedFriend);

        when(friendRepository.findByUserId(user.getId())).thenReturn(friends);

        List<Friend> result = friendService.getBlockedUsers(user.getId());

        assertTrue(result.isEmpty());
        verify(friendRepository, times(1)).findByUserId(user.getId());
    }
}
