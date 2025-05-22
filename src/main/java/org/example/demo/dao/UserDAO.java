package org.example.demo.dao;

import org.example.demo.service.DBConnection;
import org.example.demo.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void createUser(User user, String hashedPassword) throws SQLException {
        String sql = "INSERT INTO users (email, login, status, confirmation_token, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getStatus());
            stmt.setString(4, user.getConfirmToken());
            stmt.setString(5, hashedPassword);
            stmt.executeUpdate();
        }
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

    public User getUserBylogin(String login) {
       return findUserByColumn("login", login);
    }

    public User getUserByEmail(String email) {
        return findUserByColumn("email", email);
    }

    public User getUserByToken(String token) throws SQLException {
        String sql = "SELECT * FROM users WHERE confirmation_token = ?";
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, token);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return parseUserFromDB(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT id, login, email, status, confirmation_token FROM users WHERE id = ?";
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
        String sql = "UPDATE users SET status = 'ACTIVE', confirmation_token = NULL WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private User parseUserFromDB(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getString("status"),
                rs.getString("confirmation_token")
        );
    }

    private User findUserByColumn(String columnName, String value) {
        if (!"login".equals(columnName) && !"email".equals(columnName)) {
            throw new IllegalArgumentException("Invalid column name: " + columnName);
        }

        String sql = "SELECT id, login, password, email, status, confirmation_token FROM users WHERE " + columnName + " = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, value);
            try (ResultSet rs = statement.executeQuery()) {
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

