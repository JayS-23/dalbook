package com.example.facebook_integration.IntegrationTests;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@dal.ca");
        user.setSecurityAnswer("Paris");
        user.setDateOfBirth("01-01-2001");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@dal.ca");
        user.setSecurityAnswer("France");
        user.setDateOfBirth("02-02-2002");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findUserByEmail(user.getEmail());
        assertTrue(foundUser.isPresent());
        assertEquals("Jane", foundUser.get().getFirstName());
    }
}
