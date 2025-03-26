package com.mthree.etrade.model;

public class User {
    package com.mthree.etrade.model;

import javax.persistence.*;
import java.math.BigDecimal;

    @Entity
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false, length = 50)
        private String name;

        @Column(nullable = false, unique = true, length = 100)
        private String email;

        @Column(nullable = false, length = 255)
        private String password;

        @Column(nullable = false, precision = 15, scale = 2)
        private BigDecimal balance;

        // Constructors
        public User() {}

        public User(String name, String email, String password, BigDecimal balance) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.balance = balance;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        // Setters
        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        // String representation for debugging/logging
        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", balance=" + balance +
                    '}';
        }
    }

}
