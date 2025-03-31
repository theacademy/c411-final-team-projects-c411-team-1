package com.mthree.etrade;

import com.mthree.etrade.model.User;
import com.mthree.etrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        // Check if we have the test user
        if (!userService.existsByEmail("tester@gmail.com")) {
            User testUser = new User();
            testUser.setName("Test User");
            testUser.setEmail("tester@gmail.com");
            testUser.setPassword("password123"); // Will be hashed by UserService
            testUser.setBalance(new BigDecimal("10000.00"));

            userService.saveUser(testUser);
            System.out.println("Created test user with email: tester@gmail.com");
        }
    }
}