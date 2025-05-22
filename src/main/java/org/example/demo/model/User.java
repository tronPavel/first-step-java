package org.example.demo.model;

public class User {
    private int id;
    private String login;
    private String email;
    private String status;
    private String confirmToken;

    // Конструктор для создания нового пользователя (регистрация)
    public User(String login, String email, String confirmToken) {
        this.login = login;
        this.email = email;
        this.status = "PENDING";
        this.confirmToken = confirmToken;
    }

    public User(int id, String login, String email, String status, String confirmToken) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.status = status;
        this.confirmToken = confirmToken;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
    public String getConfirmToken() { return confirmToken; }

    public void setStatus(String status) { this.status = status; }
}