package com.mthree.etrade.dao;

import com.mthree.etrade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(String email);  // Find user by email
}