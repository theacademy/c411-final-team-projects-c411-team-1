package com.mthree.etrade.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

<<<<<<< HEAD
    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)
=======
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> 0fd29cfcfe645450506d2d10b369770485476414
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

<<<<<<< HEAD
    @Override    public String toString() {
=======
    @Override
    public String toString() {
>>>>>>> 0fd29cfcfe645450506d2d10b369770485476414
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(balance, user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, balance);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 0fd29cfcfe645450506d2d10b369770485476414
