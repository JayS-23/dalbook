package com.example.facebook_integration.controller;

import com.example.facebook_integration.model.User;
import com.example.facebook_integration.model.UserGroup;
import com.example.facebook_integration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    /**
     * Function: createUser
     * Purpose: Handles user signup and creates a new user.
     * Parameters: User user - The user object containing user details.
     * Returns: ResponseEntity<Integer> - The ID of the newly created user.
     */
    @PostMapping("/signup")
    public ResponseEntity<Integer> createUser(@RequestBody User user) {
        int userId = userService.createUser(user);

        logger.info("User created successfully with ID: " + userId);
        return ResponseEntity.ok(userId);
    }

    /**
     * Function: getUserByEmail
     * Purpose: Retrieves a user by their email.
     * Parameters: String email - The email of the user.
     * Returns: ResponseEntity<User> - The user object.
     */
    @GetMapping("/get/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = userService.findUserByEmail(email);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
<<<<<<< HEAD
=======
     * Function: getUserById
     * Purpose: Retrieves a user by their id.
     * Parameters: int id - The id of the user.
     * Returns: ResponseEntity<User> - The user object.
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> userOptional = userService.findUserById(id);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
>>>>>>> develop
     * Function: login
     * Purpose: Handles user login.
     * Parameters: Map<String, String> body - A map containing email and password.
     * Returns: ResponseEntity<?> - The user ID on successful login, or an error message on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        try {
            int userId = userService.login(email, password);
            return ResponseEntity.ok(userId); // Return user ID on successful login
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); // Return 401 on failure
        }
    }

    /**
     * Function: resetPassword
     * Purpose: Handles password reset.
     * Parameters: Map<String, String> requestBody - A map containing email and new password.
     * Returns: ResponseEntity<?> - A success message.
     */
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String newPassword = requestBody.get("newPassword");
        userService.updatePassword(email, newPassword);
        return ResponseEntity.ok().body("Password updated successfully");
    }

    /**
     * Function: updateStatus
     * Purpose: Updates the status of a user.
     * Parameters: Map<String, String> body - A map containing email and status.
     * Returns: void
     */
    @PutMapping("/update-status")
    public void updateStatus(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String status = body.get("status");
        userService.updateStatus(email, status);
    }

    /**
     * Function: updateUserProfile
     * Purpose: Updates the user's profile information.
     * Parameters: String firstName - The user's first name.
     *             String lastName - The user's last name.
     *             String email - The user's email.
     *             String bio - The user's bio.
     *             User.Status status - The user's status.
     *             MultipartFile profilePicture - The user's profile picture.
     * Returns: ResponseEntity<String> - A success message.
     */
    @PutMapping("/profile/update")
    public ResponseEntity<String> updateUserProfile(@RequestParam("firstName") String firstName,
                                                    @RequestParam("lastName") String lastName,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("bio") String bio,
                                                    @RequestParam("status") User.Status status,
                                                    @RequestParam("profilePicture") MultipartFile profilePicture) {
        userService.updateUserProfile(firstName, lastName, email, bio, status, profilePicture);
        return ResponseEntity.ok().body("Profile updated successfully");
    }

<<<<<<< HEAD
=======
    @GetMapping("/groups/{userId}")
    public List<UserGroup> getAllGroups(@PathVariable int userId) {
        return userService.getAllGroups(userId);
    }
>>>>>>> develop
}
