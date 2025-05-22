package org.example.demo.service;

import jakarta.mail.MessagingException;
import org.example.demo.dao.UserDAO;
import org.example.demo.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import static org.example.demo.service.MailService.sendConfirmationEmail;

public class AuthService {
    private UserDAO userDAO = new UserDAO();

    public User authenticate(String login, String password) throws SQLException {
        User user = userDAO.getUserBylogin(login);
        if (user != null && BCrypt.checkpw(password, userDAO.getPasswordByLogin(login))) {
            return user;
        }
        return null;
    }

    public void register(String login, String rawPassword, String email)
            throws SQLException, MessagingException, UnsupportedEncodingException {
        User emailExisting = userDAO.getUserByEmail(email);
        User loginExisting = userDAO.getUserBylogin(login);

        if (emailExisting != null) {
            throw new SQLException("Пользователь с таким email уже существует");
        }
        if (loginExisting != null) {
            throw new SQLException("Пользователь с таким login уже существует");
        }

        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        String token = UUID.randomUUID().toString();

        User user = new User(login, email, token);
        userDAO.createUser(user, hashedPassword); // Передаём пароль отдельно

        sendConfirmationEmail(email, token);
    }

    public boolean confirmRegistration(String token) throws SQLException {
        User user = userDAO.getUserByToken(token);
        if (user != null && "PENDING".equals(user.getStatus())) {
            userDAO.activateUser(user.getId());
            return true;
        }
        return false;
    }
}