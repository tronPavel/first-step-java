package org.example.demo.models;


public class User {
    private int id;
    private String login;
    private String password;
    private String gmail;
    private String number;


    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
}
