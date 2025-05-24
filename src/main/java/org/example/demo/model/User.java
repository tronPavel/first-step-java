package org.example.demo.model;

public class User {
    private int id;
    private String login;
    private String email;
    private String status;
    private String confirmToken;
    private String role;

    public User(String login, String email, String confirmToken) {
        this.login = login;
        this.email = email;
        this.status = "PENDING";
        this.confirmToken = confirmToken;
        this.role = "USER";
    }

    public User(int id, String login, String email, String status, String confirmToken) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.status = status;
        this.confirmToken = confirmToken;
        this.role = "USER";
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
    public String getConfirmToken() { return confirmToken; }
    public String getRole() { return role; }
    public void setStatus(String status) { this.status = status; }
    public void setRole(String role) { this.role = role; }
}