package com.mthree.etrade.controller;

import com.mthree.etrade.model.User;
import com.mthree.etrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    //Get a user by their ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //Get a list of all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }


    /*
    * Update an existing user
    */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = userService.getUserById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setBalance(updatedUser.getBalance());
        return ResponseEntity.ok(userService.saveUser(existingUser));
    }

    //Delete a user by their ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    //Get a user by their email address.
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Get a user by their email address.
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    //Check if a user exists by email.
    @PutMapping("/{id}/update-balance")
    public ResponseEntity<Void> updateBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        userService.updateBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setup-test")
    public ResponseEntity<?> setupTestUser() {
        try {
            User user = new User();
            user.setName("Test User");
            user.setEmail("test@example.com");
            user.setPassword("password");
            user.setBalance(new BigDecimal("1000.00"));

            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating test user: " + e.getMessage());
        }
    }

    @PutMapping("/validate")
    public ResponseEntity<User> validateUserInput(@RequestBody HashMap<String, String> user) {
        User newUser = new User();
        newUser.setName(user.get("name"));

        if(user.get("email").contains("@") && user.get("email").contains(".")) {
            newUser.setEmail(user.get("email"));
        }
        else {
            return new ResponseEntity<User>(newUser, HttpStatus.BAD_REQUEST);
        }

        newUser.setPassword(user.get("password"));

        try {
            newUser.setBalance(new BigDecimal(user.get("balance")));
        } catch (NumberFormatException ex) {
            return new ResponseEntity<User>(newUser, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<User>(newUser, HttpStatus.OK);
    }

}
