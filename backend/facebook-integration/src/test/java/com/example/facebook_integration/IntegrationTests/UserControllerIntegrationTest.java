package com.example.facebook_integration.IntegrationTests;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@dal.ca");
        user.setSecurityAnswer("Paris");
        user.setDateOfBirth("01-01-2001");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(createURLWithPort("/api/user/signup"), request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() > 0);
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@dal.ca");
        user.setSecurityAnswer("Paris");
        user.setDateOfBirth("01-01-2001");

        userRepository.save(user);

        ResponseEntity<User> response = restTemplate.getForEntity(createURLWithPort("/api/user/getByEmail/jane.doe@dal.ca"), User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jane", response.getBody().getFirstName());
    }

    @Test
    public void testLogin() {
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Smith");
        user.setEmail("alice.smith@dal.ca");
        user.setPassword("Password01!");
        user.setSecurityAnswer("Paris");
        user.setDateOfBirth("01-01-2001");

        userRepository.save(user);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "alice.smith@dal.ca");
        loginRequest.put("password", "Password01!");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<User> response = restTemplate.postForEntity(createURLWithPort("/api/user/login"), request, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alice", response.getBody().getFirstName());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}

