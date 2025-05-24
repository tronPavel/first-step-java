package org.example.demo.service;

import jakarta.mail.MessagingException;
import org.example.demo.dao.AuditLogDAO;
import org.example.demo.dao.UserDAO;
import org.example.demo.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static org.example.demo.service.MailService.sendConfirmationEmail;

public class AuthService {
    private static final Logger logger = LogManager.getLogger(AuthService.class);
    private UserDAO userDAO = new UserDAO();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    public User authenticate(String login, String password) throws SQLException {
        User user = userDAO.getUserByLogin(login);
        if (user != null && BCrypt.checkpw(password, userDAO.getPasswordByLogin(login))) {
            logger.debug("Authentication successful for user: {}", login);
            return user;
        }
        logger.warn("Authentication failed for user: {}", login);
        return null;
    }

    public int register(String login, String rawPassword, String email)
            throws SQLException, MessagingException, UnsupportedEncodingException {
        try (Connection conn = DBConnection.getConnection()) {
            return register(login, rawPassword, email, conn);
        }
    }

    public int register(String login, String rawPassword, String email, Connection conn)
            throws SQLException, MessagingException, UnsupportedEncodingException {
        User emailExisting = userDAO.getUserByEmail(email);
        User loginExisting = userDAO.getUserByLogin(login);

        if (emailExisting != null) {
            logger.warn("Registration failed: Email already exists: {}", email);
            throw new SQLException("Пользователь с таким email уже существует");
        }
        if (loginExisting != null) {
            logger.warn("Registration failed: Login already exists: {}", login);
            throw new SQLException("Пользователь с таким login уже существует");
        }

        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        String token = UUID.randomUUID().toString();
        String status = "PENDING";
        String role = "USER";

        User user = new User(login, email, token);
        user.setStatus(status);
        user.setRole(role);

        conn.setAutoCommit(false);
        try {
            int userId = userDAO.createUser(user, hashedPassword, conn);
            sendConfirmationEmail(email, token);
            auditLogDAO.logAction(userId, "REGISTER", "User registered with login: " + login, conn);
            conn.commit();
            logger.info("User created with login: {}, email: {}, id: {}", login, email, userId);
            return userId;
        } catch (Exception e) {
            conn.rollback();
            logger.error("Error during registration for login: {}", login, e);
            throw e;
        }
    }

    public boolean confirmRegistration(String token) throws SQLException {
        User user = userDAO.getUserByToken(token);
        if (user != null && "PENDING".equals(user.getStatus())) {
            userDAO.activateUser(user.getId());
            logger.info("User confirmed: ID={}", user.getId());
            return true;
        }
        logger.warn("Confirmation failed for token: {}", token);
        return false;
    }
}