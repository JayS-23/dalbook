package com.example.facebook_integration.UnitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.repository.FriendRequestRepository;
import com.example.facebook_integration.repository.UserRepository;
import com.example.facebook_integration.service.Implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    /* Description:
     *  Tests that a user is successfully created when valid details are provided
     *  and the email does not already exist.
     */
    void testCreateUser_Success() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        int userId = userService.createUser(user);

        assertEquals(1, userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    /* Description:
     *  Tests that an IllegalArgumentException is thrown when trying to create a user
     *  with a null user object.
     */
    void testCreateUser_UserIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null);
        });

        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that an IllegalArgumentException is thrown when trying to create a user
     *  with an email that already exists in the repository.
     */
    void testCreateUser_EmailAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a user is successfully found by their email when the email exists in the repository.
     */
    void testFindUserByEmail_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserByEmail(user.getEmail());

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    /* Description:
     *  Tests that an empty Optional is returned when the user email does not exist in the repository.
     */
    void testFindUserByEmail_NotFound() {
        when(userRepository.findUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findUserByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    /* Description:
     *  Tests that a user is successfully logged in when valid email and password are provided
     *  and the user's account is active.
     */
    void testLogin_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setIsActive(true);

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User loggedInUser = userService.login("test@example.com", "password123");

        assertNotNull(loggedInUser);
        assertEquals("test@example.com", loggedInUser.getEmail());
    }

    @Test
    /* Description:
     *  Tests that an IllegalArgumentException is thrown when trying to log in with an email
     *  that does not exist in the repository.
     */
    void testLogin_UserNotFound() {
        when(userRepository.findUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login("nonexistent@example.com", "password123");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that an IllegalArgumentException is thrown when trying to log in with an email
     *  of a user whose account has been deactivated.
     */
    void testLogin_UserDeactivated() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setIsActive(false);

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login("test@example.com", "password123");
        });

        assertEquals("Your account has been deactivated", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that an IllegalArgumentException is thrown when trying to log in with an incorrect password
     *  for an existing and active user.
     */
    void testLogin_WrongPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setIsActive(true);

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login("test@example.com", "wrongpassword");
        });

        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a user's profile is successfully updated when valid data and a non-empty profile picture
     *  are provided.
     */
    void testUpdateUserProfile_Success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        MultipartFile profilePicture = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        userService.updateUserProfile("John", "Doe", "test@example.com", "Bio", User.Status.Available, profilePicture);

        verify(userRepository).save(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("Bio", user.getBio());
        assertEquals(User.Status.Available, user.getStatus());
        assertTrue(user.getProfilePic().endsWith("/profile_pictures/test.jpg"));
    }

    @Test
    /* Description:
     *  Tests that an IllegalArgumentException is thrown when the provided email does not correspond
     *  to an existing user.
     */
    void testUpdateUserProfile_UserNotFound() {
        MultipartFile profilePicture = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserProfile("John", "Doe", "test@example.com", "Bio", User.Status.Available, profilePicture);
        });

        assertEquals("User with email test@example.com not found", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a user is successfully found when a valid ID is provided.
     */
    void testFindUserById_Success() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    /* Description:
     *  Tests that an empty Optional is returned when no user is found for the provided ID.
     */
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(1);

        assertFalse(result.isPresent());
    }

    @Test
    /* Description:
     *  Tests that all users excluding System_Admin are successfully retrieved.
     */
    void testGetAllUsers_Success() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setRole(User.Role.Student);
        User user2 = new User();
        user2.setRole(User.Role.System_Admin);
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user1, result.get(0));
    }

    @Test
    /* Description:
     *  Tests that an empty list is returned when there are no users excluding System_Admin.
     */
    void testGetAllUsers_NoUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setRole(User.Role.System_Admin);
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    /* Description:
     *  Tests that a user is successfully added when the role is System_Admin.
     */
    void testAddUser_Success() {
        User user = new User();
        user.setRole(User.Role.System_Admin);

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.addUser(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test
    /* Description:
     *  Tests that a SecurityException is thrown when the role is not System_Admin.
     */
    void testAddUser_Unauthorized() {
        User user = new User();
        user.setRole(User.Role.Student);

        SecurityException exception = assertThrows(SecurityException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a user's role is successfully updated when the admin role is System_Admin.
     */
    void testUpdateUserRole_Success() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUserRole(1, "Student", "System_Admin");

        assertNotNull(result);
        assertEquals(User.Role.Student, result.getRole());
        verify(userRepository).save(user);
    }

    @Test
    /* Description:
     *  Tests that a SecurityException is thrown when the admin role is not System_Admin.
     */
    void testUpdateUserRole_Unauthorized() {
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            userService.updateUserRole(1, "Student", "Student");
        });

        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a user is successfully deactivated when the admin role is System_Admin.
     */
    void testDeactivateUser_Success() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deactivateUser(1, "System_Admin");

        verify(userRepository).save(user);
        assertFalse(user.getIsActive());
    }

    @Test
    /* Description:
     *  Tests that a SecurityException is thrown when the admin role is not System_Admin.
     */
    void testDeactivateUser_Unauthorized() {
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            userService.deactivateUser(1, "USER");
        });

        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a user is successfully activated when the admin role is System_Admin.
     */
    void testActivateUser_Success() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.activateUser(1, "System_Admin");

        verify(userRepository).save(user);
        assertTrue(user.getIsActive());
    }

    @Test
    /* Description:
     *  Tests that a SecurityException is thrown when the admin role is not System_Admin.
     */
    void testActivateUser_Unauthorized() {
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            userService.activateUser(1, "USER");
        });

        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    /* Description:
     *  Tests that a user and their associated friend requests are successfully removed
     *  when the admin role is System_Admin.
     */
    void testRemoveUser_Success() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.removeUser(1, "System_Admin");

        verify(friendRequestRepository).deleteBySenderId(1);
        verify(friendRequestRepository).deleteByReceiverId(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    /* Description:
     *  Tests that a SecurityException is thrown when the admin role is not System_Admin.
     */
    void testRemoveUser_Unauthorized() {
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            userService.removeUser(1, "USER");
        });

        assertEquals("Unauthorized", exception.getMessage());
    }
}

