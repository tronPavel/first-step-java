package org.example.demo.dao;

import org.example.demo.service.DBConnection;
import org.example.demo.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    public int createUser(User user, String hashedPassword, Connection conn) throws SQLException {
        String sql = "INSERT INTO users (email, login, status, confirmation_token, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getStatus());
            stmt.setString(4, user.getConfirmToken());
            stmt.setString(5, hashedPassword);
            stmt.setString(6, user.getRole());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Возвращаем сгенерированный ID
                }
            }
        }
        throw new SQLException("Failed to retrieve user ID after creation");
    }

    public String getPasswordByLogin(String login) throws SQLException {
        String sql = "SELECT password FROM users WHERE login = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        }
        return null;
    }

    public User getUserByLogin(String login) {
        return findUserByColumn("login", login);
    }

    public User getUserByEmail(String email) {
        return findUserByColumn("email", email);
    }

    public User getUserByToken(String token) throws SQLException {
        String sql = "SELECT * FROM users WHERE confirmation_token = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return parseUserFromDB(rs);
                }
            }
        }
        return null;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT id, login, email, status, confirmation_token, role FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return parseUserFromDB(rs);
                }
            }
        }
        return null;
    }

    public void activateUser(int userId) throws SQLException {
        activateUser(userId, null);
    }

    public void activateUser(int userId, Connection conn) throws SQLException {
        String sql = "UPDATE users SET status = 'ACTIVE', confirmation_token = NULL WHERE id = ?";
        Connection connection = (conn != null) ? conn : DBConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } finally {
            if (conn == null && connection != null) {
                connection.close();
            }
        }
    }

    private User parseUserFromDB(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getInt("id"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getString("status"),
                rs.getString("confirmation_token")
        );
        user.setRole(rs.getString("role"));
        return user;
    }

    private User findUserByColumn(String columnName, String value) {
        if (!"login".equals(columnName) && !"email".equals(columnName)) {
            throw new IllegalArgumentException("Invalid column name: " + columnName);
        }

        String sql = "SELECT id, login, email, status, confirmation_token, role FROM users WHERE " + columnName + " = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return parseUserFromDB(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}