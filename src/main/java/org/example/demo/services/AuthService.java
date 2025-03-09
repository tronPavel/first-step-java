package org.example.demo.services;

import jakarta.mail.MessagingException;
import org.example.demo.dao.UserDAO;
import org.example.demo.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import static org.example.demo.services.MailService.sendConfirmationEmail;

public class AuthService {
    private UserDAO userDAO = new UserDAO();

    public User authenticate(String login, String password) {
        User user = userDAO.getUserBylogin(login);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void register(String login,String rawPassword ,String email) throws SQLException, MessagingException, UnsupportedEncodingException {
        User emailExisting = userDAO.getUserByEmail(email);
        User loginExisting = userDAO.getUserBylogin(login);

        if (emailExisting != null) {
            throw new SQLException("Пользователь с таким email уже существует");
        }
        if (loginExisting != null) {
            throw new SQLException("Пользователь с таким login уже существует");
        }

        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        String token = java.util.UUID.randomUUID().toString();

        User user = new User(login, hashedPassword, email,token);
        userDAO.createUser(user);

        sendConfirmationEmail(email, token);
    }

    public boolean confirmRegistration(String token) throws SQLException {
        User user = userDAO.getUserByToken(token);
        System.out.println("user in confim");
        System.out.println(user.getStatus());

        if (user != null && "PENDING".equals(user.getStatus())) {
            userDAO.activateUser(user.getId());
            return true;
        }
        return false;
    }

}