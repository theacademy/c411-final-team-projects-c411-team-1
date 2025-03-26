package com.mthree.etrade.service;
import com.mthree.etrade.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUserById(Long id);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Additional method for updating user balance
    void updateBalance(Long userId, BigDecimal amount);
}
