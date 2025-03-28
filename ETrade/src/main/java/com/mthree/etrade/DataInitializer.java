package com.mthree.etrade;

import com.mthree.etrade.model.User;
import com.mthree.etrade.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        // Check if we have any users already
        if (userService.getAllUsers().isEmpty()) {
            User user = new User();
            user.setName("Test User");
            user.setEmail("test@example.com");
            user.setPassword("password");
            user.setBalance(new BigDecimal("1000.00"));

            userService.saveUser(user);
            System.out.println("Created test user");
        }
    }
}