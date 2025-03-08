package org.example.demo.services;

import org.example.demo.dao.UserDAO;
import org.example.demo.models.User;

public class AuthService {
    private UserDAO userDAO = new UserDAO();

    public User authenticate(String login, String password) {
        User user = userDAO.getUserBylogin(login);
        if (user != null && user.getPassword().equals(password)) { // TODO шифрование
            return user;
        }
        return null;
    }
}