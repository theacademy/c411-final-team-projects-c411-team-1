package com.mthree.etrade.service;

import com.mthree.etrade.dao.UserDao;
import com.mthree.etrade.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Alice Test");
        testUser.setEmail("alice@test.com");
        testUser.setPassword("password123");
        testUser.setBalance(new BigDecimal("1000.00"));
        testUser = userService.saveUser(testUser);
    }

    @Test
    void testSaveUser() {
        assertNotNull(testUser.getId());
        assertEquals("Alice Test", testUser.getName());
    }

    @Test
    void testGetUserById() {
        User found = userService.getUserById(testUser.getId());
        assertEquals(testUser.getEmail(), found.getEmail());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertFalse(users.isEmpty());
    }

    @Test
    void testFindByEmail() {
        assertTrue(userService.findByEmail("alice@test.com").isPresent());
    }

    @Test
    void testExistsByEmail() {
        assertTrue(userService.existsByEmail("alice@test.com"));
        assertFalse(userService.existsByEmail("nonexistent@test.com"));
    }

    @Test
    void testUpdateBalance() {
        BigDecimal addAmount = new BigDecimal("500.00");
        userService.updateBalance(testUser.getId(), addAmount);

        User updated = userService.getUserById(testUser.getId());
        assertEquals(new BigDecimal("1500.00"), updated.getBalance());
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(testUser.getId());
        assertFalse(userDao.findById(testUser.getId()).isPresent());
    }
}
