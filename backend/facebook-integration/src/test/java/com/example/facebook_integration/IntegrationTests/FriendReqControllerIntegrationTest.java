package com.example.facebook_integration.IntegrationTests;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.repository.UserRepository;
import com.example.facebook_integration.model.FriendRequest;
import com.example.facebook_integration.repository.FriendRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FriendReqControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private User senderUser() {
        User user = new User();
        user.setFirstName("Sender");
        user.setLastName("User");
        user.setEmail("sender@dal.ca");
        user.setSecurityAnswer("Paris");
        user.setDateOfBirth("01-01-2001");
        return userRepository.save(user);
    }

    private User receiverUser() {
        User user = new User();
        user.setFirstName("Receiver");
        user.setLastName("User");
        user.setEmail("receiver@dal.ca");
        user.setSecurityAnswer("France");
        user.setDateOfBirth("02-02-2002");
        return userRepository.save(user);
    }

    private User user() {
        User user = new User();
        user.setFirstName("User");
        user.setLastName("User");
        user.setEmail("user@dal.ca");
        user.setSecurityAnswer("Canada");
        user.setDateOfBirth("03-03-2003");
        return userRepository.save(user);
    }

    private User friend() {
        User user = new User();
        user.setFirstName("Friend");
        user.setLastName("User");
        user.setEmail("friend@dal.ca");
        user.setSecurityAnswer("US");
        user.setDateOfBirth("04-04-2004");
        return userRepository.save(user);
    }

    @Test
    public void testSendRequest() throws Exception {
        User sender = senderUser();
        User receiver = receiverUser();

        // When
        ResponseEntity<FriendRequest> response = restTemplate.postForEntity(
                createURLWithPort("/api/friends/send?senderId=" + sender.getId() + "&receiverEmail=" + receiver.getEmail()),
                null,
                FriendRequest.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FriendRequest responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(sender.getId(), responseBody.getSender().getId());
        assertEquals(receiver.getEmail(), responseBody.getReceiver().getEmail());
    }

    @Test
    public void testAcceptRequest() {
        // Given
        User sender = senderUser();
        User receiver = receiverUser();

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setAccepted(false);
        friendRequestRepository.save(request);

        // When
        ResponseEntity<FriendRequest> response = restTemplate.postForEntity(
                createURLWithPort("/api/friends/accept?requestId=" + request.getId()),
                null,
                FriendRequest.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FriendRequest responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isAccepted());
    }

    @Test
    public void testGetPendingRequests() {
        // Given
        User sender1 = senderUser();
        User sender2 = user();
        User receiver = receiverUser();

        FriendRequest request1 = new FriendRequest();
        request1.setSender(sender1);
        request1.setReceiver(receiver);
        request1.setAccepted(false);
        friendRequestRepository.save(request1);

        FriendRequest request2 = new FriendRequest();
        request2.setSender(sender2);
        request2.setReceiver(receiver);
        request2.setAccepted(false);
        friendRequestRepository.save(request2);

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                createURLWithPort("/api/friends/pending?userId=" + receiver.getId()),
                List.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FriendRequest> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    public void testGetFriends() {
        // Given
        User user = senderUser();
        User friend1 = receiverUser();
        User friend2 = friend();

        FriendRequest request1 = new FriendRequest();
        request1.setSender(user);
        request1.setReceiver(friend1);
        request1.setAccepted(true);
        friendRequestRepository.save(request1);

        FriendRequest request2 = new FriendRequest();
        request2.setSender(user);
        request2.setReceiver(friend2);
        request2.setAccepted(true);
        friendRequestRepository.save(request2);

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                createURLWithPort("/api/friends/list?userId=" + user.getId()),
                List.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<User> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    public void testDeleteFriend() {
        // Given
        User user = user();
        User friend = friend();

        FriendRequest request = new FriendRequest();
        request.setSender(user);
        request.setReceiver(friend);
        request.setAccepted(true);
        friendRequestRepository.save(request);

        // When
        restTemplate.delete(createURLWithPort("/api/friends/delete?userId=" + user.getId() + "&friendId=" + friend.getId()));

        // Then
        ResponseEntity<List> response = restTemplate.getForEntity(
                createURLWithPort("/api/friends/list?userId=" + user.getId()),
                List.class);
        List<User> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }
}

