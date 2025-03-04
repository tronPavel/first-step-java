package org.example.demo.models;


public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;

    public User(int id, String username, String password, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }
}
