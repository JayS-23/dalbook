package com.example.facebook_integration.UnitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.facebook_integration.exception.FriendRequestAlreadyExistsException;
import com.example.facebook_integration.model.FriendRequest;
import com.example.facebook_integration.model.User;
import com.example.facebook_integration.repository.FriendRequestRepository;
import com.example.facebook_integration.repository.UserRepository;
import com.example.facebook_integration.service.Implementations.FriendRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendRequestServiceImpl friendRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    /* Description:
     *  Tests that a friend request is successfully sent from one user to another
     *  when valid sender and receiver IDs are provided.
     */
    void testSendRequest_Success() {
        User sender = new User();
        User receiver = new User();

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));

        FriendRequest friendRequest = new FriendRequest(sender, receiver, false);
        when(friendRequestRepository.save(any(FriendRequest.class))).thenReturn(friendRequest);

        FriendRequest result = friendRequestService.sendRequest(1, 2);

        assertNotNull(result);
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        verify(friendRequestRepository).save(any(FriendRequest.class));
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the sender ID does not exist
     *  in the repository.
     */
    void testSendRequest_SenderNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.sendRequest(1, 2);
        });

        assertEquals("Sender not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the receiver ID does not exist
     *  in the repository.
     */
    void testSendRequest_ReceiverNotFound() {
        User sender = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.sendRequest(1, 2);
        });

        assertEquals("Receiver not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a friend request is successfully sent from one user to another using their email
     *  when valid sender ID and receiver email are provided.
     */
    void testSendRequestByEmail_Success() {
        User sender = new User();
        User receiver = new User();

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(receiver));
        when(friendRequestRepository.findBySenderAndReceiver(any(User.class), any(User.class))).thenReturn(new ArrayList<>());

        FriendRequest friendRequest = new FriendRequest(sender, receiver, false);
        when(friendRequestRepository.save(any(FriendRequest.class))).thenReturn(friendRequest);

        FriendRequest result = friendRequestService.sendRequestByEmail(1, "test@example.com");

        assertNotNull(result);
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        verify(friendRequestRepository).save(any(FriendRequest.class));
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the sender ID does not exist
     *  in the repository.
     */
    void testSendRequestByEmail_SenderNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.sendRequestByEmail(1, "test@example.com");
        });

        assertEquals("Sender not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the receiver email does not exist
     *  in the repository.
     */
    void testSendRequestByEmail_ReceiverNotFound() {
        User sender = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.sendRequestByEmail(1, "test@example.com");
        });

        assertEquals("No user found with email: test@example.com", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a FriendRequestAlreadyExistsException is thrown when trying to send
     *  a friend request to a user who has already received a pending friend request.
     */
    void testSendRequestByEmail_RequestAlreadySent() {
        User sender = new User();
        User receiver = new User();
        List<FriendRequest> requests = new ArrayList<>();
        requests.add(new FriendRequest(sender, receiver, false));

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(receiver));
        when(friendRequestRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(requests);

        FriendRequestAlreadyExistsException exception = assertThrows(FriendRequestAlreadyExistsException.class, () -> {
            friendRequestService.sendRequestByEmail(1, "test@example.com");
        });

        assertEquals("Friend request already sent", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a FriendRequestAlreadyExistsException is thrown when trying to send
     *  a friend request to a user who is already a friend.
     */
    void testSendRequestByEmail_AlreadyFriends() {
        User sender = new User();
        User receiver = new User();
        List<FriendRequest> requests = new ArrayList<>();
        requests.add(new FriendRequest(sender, receiver, true));

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(receiver));
        when(friendRequestRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(requests);

        FriendRequestAlreadyExistsException exception = assertThrows(FriendRequestAlreadyExistsException.class, () -> {
            friendRequestService.sendRequestByEmail(1, "test@example.com");
        });

        assertEquals("Already friends", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a friend request is successfully accepted when a valid request ID is provided.
     */
    void testAcceptRequest_Success() {
        FriendRequest friendRequest = new FriendRequest();
        when(friendRequestRepository.findById(1)).thenReturn(Optional.of(friendRequest));

        friendRequest.setAccepted(true);
        when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);

        FriendRequest result = friendRequestService.acceptRequest(1);

        assertNotNull(result);
        assertTrue(result.isAccepted());
        verify(friendRequestRepository).save(friendRequest);
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the request ID does not exist
     *  in the repository.
     */
    void testAcceptRequest_RequestNotFound() {
        when(friendRequestRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.acceptRequest(1);
        });

        assertEquals("Request not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that pending friend requests for a user are successfully retrieved
     *  when a valid user ID is provided.
     */
    void testGetPendingRequests_Success() {
        User receiver = new User();
        List<FriendRequest> requests = new ArrayList<>();
        when(userRepository.findById(1)).thenReturn(Optional.of(receiver));
        when(friendRequestRepository.findByReceiverAndAcceptedFalse(receiver)).thenReturn(requests);

        List<FriendRequest> result = friendRequestService.getPendingRequests(1);

        assertNotNull(result);
        assertEquals(requests, result);
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the user ID does not exist
     *  in the repository.
     */
    void testGetPendingRequests_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.getPendingRequests(1);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that friends of a user are successfully retrieved when a valid user ID is provided.
     */
    void testGetFriends_Success() {
        User user = new User();
        List<FriendRequest> sentRequests = new ArrayList<>();
        List<FriendRequest> receivedRequests = new ArrayList<>();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(friendRequestRepository.findBySenderAndAcceptedTrue(user)).thenReturn(sentRequests);
        when(friendRequestRepository.findByReceiverAndAcceptedTrue(user)).thenReturn(receivedRequests);

        List<User> result = friendRequestService.getFriends(1);

        assertNotNull(result);
        assertTrue(result.isEmpty()); // Assuming no friends in mock setup
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the user ID does not exist
     *  in the repository.
     */
    void testGetFriends_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.getFriends(1);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that the statusBySenderAndReceiver method returns 0 when no friend request exists
     *  between the sender and receiver.
     */
    void testStatusBySenderAndReceiver_NoRequest() {
        User sender = new User();
        User receiver = new User();
        when(friendRequestRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(new ArrayList<>());

        int status = friendRequestService.statusBySenderAndReceiver(sender, receiver);

        assertEquals(0, status);
    }

    @Test
    /* Description:
     *  Tests that the statusBySenderAndReceiver method returns -1 when a pending friend request exists
     *  between the sender and receiver.
     */
    void testStatusBySenderAndReceiver_PendingRequest() {
        User sender = new User();
        User receiver = new User();
        List<FriendRequest> requests = new ArrayList<>();
        requests.add(new FriendRequest(sender, receiver, false));
        when(friendRequestRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(requests);

        int status = friendRequestService.statusBySenderAndReceiver(sender, receiver);

        assertEquals(-1, status);
    }

    @Test
    /* Description:
     *  Tests that the statusBySenderAndReceiver method returns 1 when the sender and receiver are already friends.
     */
    void testStatusBySenderAndReceiver_AlreadyFriends() {
        User sender = new User();
        User receiver = new User();
        List<FriendRequest> requests = new ArrayList<>();
        requests.add(new FriendRequest(sender, receiver, true));
        when(friendRequestRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(requests);

        int status = friendRequestService.statusBySenderAndReceiver(sender, receiver);

        assertEquals(1, status);
    }

    @Test
    /* Description:
     *  Tests that a friend is successfully deleted from a user's friend list
     *  when valid user ID and friend ID are provided.
     */
    void testDeleteFriend_Success() {
        User user = new User();
        User friend = new User();
        List<FriendRequest> requestsAsSender = new ArrayList<>();
        List<FriendRequest> requestsAsReceiver = new ArrayList<>();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(friend));
        when(friendRequestRepository.findBySenderAndReceiver(user, friend)).thenReturn(requestsAsSender);
        when(friendRequestRepository.findBySenderAndReceiver(friend, user)).thenReturn(requestsAsReceiver);

        friendRequestService.deleteFriend(1, 2);

        verify(friendRequestRepository, times(requestsAsSender.size())).delete(any(FriendRequest.class));
        verify(friendRequestRepository, times(requestsAsReceiver.size())).delete(any(FriendRequest.class));
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the user ID does not exist
     *  in the repository.
     */
    void testDeleteFriend_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.deleteFriend(1, 2);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a RuntimeException is thrown when the friend ID does not exist
     *  in the repository.
     */
    void testDeleteFriend_FriendNotFound() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendRequestService.deleteFriend(1, 2);
        });

        assertEquals("Friend not found", exception.getMessage());
    }
}

