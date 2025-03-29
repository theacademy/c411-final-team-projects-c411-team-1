package com.mthree.etrade.service;

import com.mthree.etrade.dao.UserDao;
import com.mthree.etrade.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userDao.existsById(id)) {
            throw new RuntimeException("Cannot delete. User with ID " + id + " does not exist.");
        }
        userDao.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userDao.findByEmail(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.findByEmail(email) != null;
    }

    @Override
    public void updateBalance(Long userId, BigDecimal amount) {
        User user = getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userDao.save(user);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
