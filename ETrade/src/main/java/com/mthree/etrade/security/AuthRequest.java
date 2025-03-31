package com.mthree.etrade.security;

import java.util.Objects;

public class AuthRequest {

    private String email;
    private String password;

    // Default constructor needed for Jackson
    public AuthRequest() {
    }

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
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
  
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AuthRequest)) return false;
        AuthRequest that = (AuthRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
