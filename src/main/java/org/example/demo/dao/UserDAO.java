package org.example.demo.dao;

import org.example.demo.service.DBConnection;
import org.example.demo.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password, login, status, confirmation_token) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getLogin());
            stmt.setString(4, user.getStatus());
            stmt.setString(5, user.getConfirmToken());
            stmt.executeUpdate();
        }
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
                rs.getString("password"),
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

