package com.example.projectprm392.entity;

public class User {
    private int userID;
    private String Username;
    private String email;
    private String Password;
    private String Role;

    public User() {

    }

    public User(int userID, String username, String email, String password, String role) {
        this.userID = userID;
        Username = username;
        this.email = email;
        Password = password;
        Role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
