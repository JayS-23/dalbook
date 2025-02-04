package com.example.facebook_integration.IntegrationTests;

import com.example.facebook_integration.model.FriendRequest;
import com.example.facebook_integration.model.User;
import com.example.facebook_integration.repository.FriendRequestRepository;
import com.example.facebook_integration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class FriendReqRepoIntegrationTest {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        friendRequestRepository.deleteAll();
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

    @Test
    public void testFindByReceiverAndAcceptedFalse() {
        User sender = senderUser();
        User receiver = receiverUser();
        friendRequestRepository.save(new FriendRequest(sender, receiver, false));

        List<FriendRequest> requests = friendRequestRepository.findByReceiverAndAcceptedFalse(receiver);
        assertEquals(1, requests.size());
        assertTests(sender, receiver, requests);
        assertFalse(requests.get(0).isAccepted());
    }

    @Test
    public void testFindBySenderAndAcceptedFalse() {
        User sender = senderUser();
        User receiver = receiverUser();
        friendRequestRepository.save(new FriendRequest(sender, receiver, false));

        List<FriendRequest> requests = friendRequestRepository.findBySenderAndAcceptedFalse(sender);
        assertEquals(1, requests.size());
        assertTests(sender, receiver, requests);
        assertFalse(requests.get(0).isAccepted());
    }

    @Test
    public void testFindBySenderAndAcceptedTrue() {
        User sender = senderUser();
        User receiver = receiverUser();
        friendRequestRepository.save(new FriendRequest(sender, receiver, true));

        List<FriendRequest> requests = friendRequestRepository.findBySenderAndAcceptedTrue(sender);
        assertEquals(1, requests.size());
        assertTests(sender, receiver, requests);
        assertTrue(requests.get(0).isAccepted());
    }

    @Test
    public void testFindByReceiverAndAcceptedTrue() {
        User sender = senderUser();
        User receiver = receiverUser();
        friendRequestRepository.save(new FriendRequest(sender, receiver, true));

        List<FriendRequest> requests = friendRequestRepository.findByReceiverAndAcceptedTrue(receiver);
        assertEquals(1, requests.size());
        assertTests(sender, receiver, requests);
        assertTrue(requests.get(0).isAccepted());
    }

    @Test
    public void testFindBySenderAndReceiver() {
        User sender = senderUser();
        User receiver = receiverUser();
        friendRequestRepository.save(new FriendRequest(sender, receiver, false));

        List<FriendRequest> requests = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        assertEquals(1, requests.size());
        assertTests(sender, receiver, requests);
    }

    @Test
    public void testDeleteBySenderId() {
        User sender = senderUser();
        User receiver = receiverUser();
        friendRequestRepository.save(new FriendRequest(sender, receiver, false));

        friendRequestRepository.deleteBySenderId(sender.getId());

        List<FriendRequest> requests = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        assertTrue(requests.isEmpty());
    }

    @Test
    public void testDeleteByReceiverId() {
        User sender = senderUser();
        User receiver = receiverUser();
        friendRequestRepository.save(new FriendRequest(sender, receiver, false));

        friendRequestRepository.deleteByReceiverId(receiver.getId());

        List<FriendRequest> requests = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        assertTrue(requests.isEmpty());
    }

    private void assertTests(User sender, User receiver, List<FriendRequest> requests) {
        assertEquals(sender, requests.get(0).getSender());
        assertEquals(receiver, requests.get(0).getReceiver());
    }
}

