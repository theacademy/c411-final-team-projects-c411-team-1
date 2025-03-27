package com.mthree.etrade.dao;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        // Clean the database before each test
        userDao.deleteAll();
    }

    @Test
    void testAddAndFindById() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setBalance(new BigDecimal("1000"));
        user = userDao.save(user);

        User retrievedUser = userDao.findById(user.getId()).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void testUpdate() {
        //comment
        User user = new User();
        user.setName("Old Name");
        user.setEmail("oldemail@example.com");
        user.setPassword("password123");
        user.setBalance(new BigDecimal("1000"));
        user = userDao.save(user);

        // Update the user's details
        user.setName("New Name");
        user = userDao.save(user);

        User updatedUser = userDao.findById(user.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setName("Delete Me");
        user.setEmail("delete@example.com");
        user.setPassword("password123");
        user.setBalance(new BigDecimal("1000"));
        user = userDao.save(user);

        // Ensure user exists before deletion
        assertNotNull(userDao.findById(user.getId()).orElse(null));

        userDao.deleteById(user.getId());

        // Ensure user is deleted
        assertNull(userDao.findById(user.getId()).orElse(null));
    }
}
